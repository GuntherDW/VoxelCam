package com.thatapplefreak.voxelcam.net;

/**
 * A callback for programs that want to do something once a request is complete.
 * 
 *
 * @param <R> The response class
 */
public interface Callback<R> {

	/**
	 * Called when a request completes and was technically successful. May not
	 * have actually succeeded because of differences in server implementation.
	 * Check the response for details.
	 * 
	 * @param response The response
	 */
	void onSuccess(R response);

	/**
	 * Called if an error occured on the client or over the network.
	 * 
	 * @param t The error
	 */
	void onFailure(Throwable t);

}
