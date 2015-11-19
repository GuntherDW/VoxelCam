package com.thatapplefreak.voxelcam.net.twitter;

import com.thatapplefreak.voxelcam.net.auth.OAuth2;

public class TwitterAuth implements OAuth2 {

	private static final String CONSUMER_KEY = "okIIDosE4TsrRP3JvXufw";
	private static final String CONSUMER_SECRET = "dFJIErDmYr61YwQfDdAGMAt79dCJGu1mpiflCAa2c";
	private static final String REQUEST_TOKEN_URL = "https://api.twitter.com/oauth/request_token";
	private static final String ACCESS_TOKEN_URL = "https://api.twitter.com/oauth/access_token";
	private static final String AUTHORIZE_URL = "https://api.twitter.com/oauth/authorize";

	@Override
	public String getConsumerKey() {
		return CONSUMER_KEY;
	}

	@Override
	public String getConsumerSecret() {
		return CONSUMER_SECRET;
	}

	@Override
	public String getAuthorizeUrl() {
		return AUTHORIZE_URL;
	}

	@Override
	public String getAccessTokenUrl() {
		return ACCESS_TOKEN_URL;
	}

	@Override
	public String getRequestTokenUrl() {
		return REQUEST_TOKEN_URL;
	}

	@Override
	public String getName() {
		return "Twitter";
	}

}
