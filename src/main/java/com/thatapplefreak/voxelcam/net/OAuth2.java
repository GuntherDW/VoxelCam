package com.thatapplefreak.voxelcam.net;

import java.net.URL;

public interface OAuth2 {
	
	String getName();

	String getConsumerKey();

	String getConsumerSecret();

	String getRequestTokenUrl();

	String getAccessTokenUrl();

	String getAuthorizeUrl();

	/**
	 * Used to send the user to the provided URL in order to create a pin.
	 * 
	 * @param auth The url to open
	 * @return The pin which was given to the user
	 */
	String authorizeUser(URL auth);

}
