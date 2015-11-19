package com.thatapplefreak.voxelcam.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Base64;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.google.common.collect.Maps;
import com.google.common.io.Closeables;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.mumfrey.liteloader.core.LiteLoader;
import com.mumfrey.liteloader.modconfig.ConfigStrategy;
import com.mumfrey.liteloader.modconfig.Exposable;
import com.mumfrey.liteloader.modconfig.ExposableOptions;
import com.thatapplefreak.voxelcam.net.auth.AuthFetcher;
import com.thatapplefreak.voxelcam.net.auth.Authorizer;
import com.thatapplefreak.voxelcam.net.auth.OAuth2;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthException;

@ExposableOptions(strategy = ConfigStrategy.Unversioned, filename = "voxelcamtokens")
public class Poster implements Exposable {

	@Expose
	private Map<String, String> tokens = Maps.newHashMap();

	public <R> void post(Request<R> request) throws OAuthException, IOException, PayloadException {
		try {
			RequestBuilder builder = RequestBuilder.create(request.getMethod().toString());
			builder.setUri(request.getRequestUrl());

			if (request instanceof Payload) {
				if (request instanceof MultipartPayload) {
					MultipartEntityBuilder data = MultipartEntityBuilder.create();
					((MultipartPayload) request).assemblePayload(data);
					builder.setEntity(data.build());
				} else if (request instanceof BasicPayload) {
					((BasicPayload) request).assemblePayload(builder);
				} else {
					throw new PayloadException("No payload found");
				}
			}
			HttpUriRequest post = builder.build();
			if (request instanceof Authorizer) {
				String id = ((Authorizer) request).getAuthorization();
				post.addHeader(Authorizer.HEADER, id);
			}
			if (request instanceof OAuth2) {
				authenticate2((OAuth2) request).sign(post);
			}
			HttpClient client = HttpClientBuilder.create().build();
			HttpResponse response = client.execute(post);

			StatusLine status = response.getStatusLine();
			R r = createResponse(response.getEntity(), request.getResponseClass());
			request.onResponse(new Response<R>(status, r));
		} catch (Exception e) {
			e.printStackTrace();
			request.onFailure(e);
		}
	}

	private <T> T createResponse(HttpEntity entity, Class<T> type) throws IOException {
		InputStream in = null;
		Reader read = null;
		try {
			in = entity.getContent();
			read = new InputStreamReader(in);
			Gson gson = new Gson();
			return gson.fromJson(read, type);
		} finally {
			Closeables.closeQuietly(in);
			Closeables.closeQuietly(read);
			EntityUtils.consumeQuietly(entity);
		}
	}

	public void authenticate(final OAuth2 auth) {
		new Thread() {
			@Override
			public void run() {
				try {
					authenticate2(auth);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	private OAuthConsumer authenticate2(OAuth2 oauth) throws OAuthException, IOException {
		OAuthConsumer consumer = new CommonsHttpOAuthConsumer(oauth.getConsumerKey(), oauth.getConsumerSecret());
		String name = getName(oauth);
		Pair<String, String> token = getToken(name);
		if (getToken(name) == null) {
			OAuthProvider provider = new CommonsHttpOAuthProvider(oauth.getRequestTokenUrl(), oauth.getAccessTokenUrl(),
					oauth.getAuthorizeUrl());
			URL auth = new URL(provider.retrieveRequestToken(consumer, OAuth.OUT_OF_BAND));
			String pin = AuthFetcher.getPin(auth, oauth);
			if (pin != null) {
				provider.retrieveAccessToken(consumer, pin);
				setToken(name, consumer.getToken(), consumer.getTokenSecret());
			}
		} else {
			consumer.setTokenWithSecret(token.getLeft(), token.getRight());
		}
		return consumer;
	}

	public void invalidate(String name) {
		tokens.remove(name);
		saveTokens();
	}

	public static String getName(OAuth2 oauth) {
		String name = oauth.getName();
		if (name == null) {
			name = oauth.getClass().getSimpleName();
		}
		return name;
	}

	private void setToken(String name, String token, String secret) {
		String value = String.format("%s:%s", token, secret);
		// base64 encode to deter tampering
		value = new String(Base64.getUrlEncoder().encode(value.getBytes()));
		tokens.put(name, value);
		saveTokens();
	}

	private Pair<String, String> getToken(String name) {
		String token = tokens.get(name);
		Pair<String, String> pair = null;
		if (token != null) {
			token = new String(Base64.getUrlDecoder().decode(token.getBytes()));

			int indx = token.indexOf(':');
			if (indx > 0) {
				String left = token.substring(0, indx);
				String right = token.substring(indx + 1);
				pair = Pair.of(left, right);
			}
		}
		return pair;
	}

	private void saveTokens() {
		LiteLoader.getInstance().writeConfig(this);
	}

	public boolean isLoggedIn(String name) {
		return this.tokens.containsKey(name);
	}
}
