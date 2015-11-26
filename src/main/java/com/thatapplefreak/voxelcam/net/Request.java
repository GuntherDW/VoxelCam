package com.thatapplefreak.voxelcam.net;

/**
 * Implement this to create a request.
 *
 * @param <R>
 */
public interface Request<R> {

	/**
	 * Gets the URL to connect to.
	 * 
	 * @return The url
	 */
	String getRequestUrl();

	/**
	 * The response class to use for gson deserialization.
	 * 
	 * @return The response class
	 */
	Class<R> getResponseClass();

	/**
	 * The method to use when connecting.
	 * 
	 * @return The method
	 */
	Method getMethod();

}
