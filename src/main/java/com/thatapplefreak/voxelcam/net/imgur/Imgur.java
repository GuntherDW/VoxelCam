package com.thatapplefreak.voxelcam.net.imgur;

import com.thatapplefreak.voxelcam.net.Request;
import com.thatapplefreak.voxelcam.net.auth.Authorizer;

/**
 * Base class for objects which perform manipulation actions against the imgur
 * API
 */
public abstract class Imgur<T extends ImgurResponse<?>> implements Request<T>, Authorizer {

	protected static final String API_URL = "https://api.imgur.com/3/image/%s";
	private static final String CLIENT_ID = "b0118040d2b06e2";

	private final String id;

	public Imgur(String id) {
		this.id = id;
	}

	@Override
	public String getRequestUrl() {
		return String.format(API_URL, id);
	}

	@Override
	public String getAuthorization() {
		return String.format("Client-ID %s", CLIENT_ID);
	}
}