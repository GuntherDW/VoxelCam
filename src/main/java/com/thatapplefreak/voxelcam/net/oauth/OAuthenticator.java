package com.thatapplefreak.voxelcam.net.oauth;

import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.annotations.Expose;
import com.mumfrey.liteloader.core.LiteLoader;
import com.mumfrey.liteloader.modconfig.ConfigStrategy;
import com.mumfrey.liteloader.modconfig.Exposable;
import com.mumfrey.liteloader.modconfig.ExposableOptions;
import com.thatapplefreak.voxelcam.net.MultipartPayload;
import com.thatapplefreak.voxelcam.net.PayloadException;
import com.thatapplefreak.voxelcam.net.Request;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthException;

@ExposableOptions(strategy = ConfigStrategy.Unversioned, filename = "voxelcamtokens")
public class OAuthenticator implements Exposable {

	@Expose
	private Map<String, String> tokens;

	public void doThing(Request<?> request) throws OAuthException, IOException, PayloadException {
		HttpPost post = new HttpPost(request.getRequestUrl());
		if (request instanceof OAuth2) {
			authenticate((OAuth2) request).sign(post);
		}
		HttpEntity entity = getEntity(request);
		post.setEntity(entity);
		HttpClient client = HttpClientBuilder.create().build();
		client.execute(post);
	}

	private HttpEntity getEntity(Request<?> request) throws PayloadException {
		if (request instanceof MultipartPayload) {
			MultipartEntityBuilder payload = MultipartEntityBuilder.create();
			((MultipartPayload<?>) request).assemblePayload(payload);
			return payload.build();
		}
		return null;
	}

	private OAuthConsumer authenticate(OAuth2 oauth) throws OAuthException, IOException {
		OAuthConsumer consumer = new CommonsHttpOAuthConsumer(oauth.getConsumerKey(), oauth.getConsumerSecret());
		String name = getName(oauth);
		Pair<String, String> token = getToken(name);
		if (getToken(name) == null) {
			OAuthProvider provider = new CommonsHttpOAuthProvider(oauth.getRequestTokenUrl(), oauth.getAccessTokenUrl(),
					oauth.getAuthorizeUrl());
			URL auth = new URL(provider.retrieveRequestToken(consumer, OAuth.OUT_OF_BAND));
			String pin = oauth.authorizeUser(auth);
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
		value = Base64.getUrlEncoder().encodeToString(token.getBytes());
		tokens.put(name, value);
		saveTokens();
	}

	private Pair<String, String> getToken(String name) {
		String token = tokens.get(name);
		Pair<String, String> pair = null;
		if (token != null) {
			token = new String(Base64.getUrlDecoder().decode(token));
			int indx = token.indexOf(':');
			String left = token.substring(0, indx);
			String right = token.substring(indx + 1);
			pair = Pair.of(left, right);
		}
		return pair;
	}

	private void saveTokens() {
		LiteLoader.getInstance().writeConfig(this);
	}
}
