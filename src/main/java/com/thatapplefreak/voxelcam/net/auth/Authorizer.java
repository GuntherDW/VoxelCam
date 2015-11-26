package com.thatapplefreak.voxelcam.net.auth;

public interface Authorizer {

	/**
	 * The header key for the authorization
	 */
	String HEADER = "Authorization";

	/**
	 * Gets the authorization value for this request.
	 *
	 * @return The authorization to be included in the header
	 */
	String getAuthorization();
}
