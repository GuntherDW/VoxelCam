package com.thatapplefreak.voxelcam.net.auth;

public interface OAuth2 {
	
	String getName();

	String getConsumerKey();

	String getConsumerSecret();

	String getRequestTokenUrl();

	String getAccessTokenUrl();

	String getAuthorizeUrl();

}
