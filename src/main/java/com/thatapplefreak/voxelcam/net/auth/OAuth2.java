package com.thatapplefreak.voxelcam.net.auth;

/**
 * Interface for requests that need to be authorized via OAuth2.
 */
public interface OAuth2 {

	/**
	 * Gets the name or ID for saving purposes. Leaving null will default to the
	 * class name.
	 * 
	 * @return The name or ID
	 */
	String getName();

	/**
	 * Gets the consumer key for this application.
	 * 
	 * @return The consumer key
	 */
	String getConsumerKey();

	/**
	 * Gets the consumer secret for this application. If there is none, return
	 * empty string. <code>""</code>
	 * 
	 * @return The consumer secret
	 */
	String getConsumerSecret();

	/**
	 * Gets the url used to get a request token
	 * 
	 * @return The request token url
	 */
	String getRequestTokenUrl();

	/**
	 * Gets the url used to get an access token
	 * 
	 * @return The access token url
	 */
	String getAccessTokenUrl();

	/**
	 * Gets the url used to authorize the user with the token.
	 * 
	 * @return The authorize url
	 */
	String getAuthorizeUrl();

}
