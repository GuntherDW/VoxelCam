package com.thatapplefreak.voxelcam.net;

/**
 * Base interface for requests that wish to provide a payload. Use one of the
 * sub-interfaces instead.
 * 
 * @param <Data> The data builder type
 */
interface Payload<Data> {

	/**
	 * Used to build the payload.
	 * 
	 * @param data The builder object
	 */
	void assemblePayload(Data data);
}
