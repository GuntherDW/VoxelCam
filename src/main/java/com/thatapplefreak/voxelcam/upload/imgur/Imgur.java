package com.thatapplefreak.voxelcam.upload.imgur;

import com.thatapplefreak.voxelcam.upload.Upload;

/**
 * Base class for objects which perform manipulation actions against the imgur
 * API
 * 
 * @author Mumfrey
 */
public abstract class Imgur<T extends ImgurResponse> extends Upload<T> {

	/**
	 * Base url for the imgur API
	 */
	public static final String IMGUR_API_BASEURL = "https://api.imgur.com";

	/**
	 * imgur API version to call
	 */
	public static final int IMGUR_API_VERSION = 3;

	/**
	 * Our client id
	 */
	public static final String IMGUR_API_CLIENTID = "b0118040d2b06e2";

	public Imgur(Method method, String route, Class<T> responseClass) {
		this(method, route, responseClass, null);
	}

	public Imgur(Method method, String route, Class<T> responseClass, String get) {
		super(method, getUrl(route, get), responseClass);
	}

	private static String getUrl(String route, String get) {
		return String.format("%s/%d/%s%s%s", Imgur.IMGUR_API_BASEURL, Imgur.IMGUR_API_VERSION, route.toLowerCase(),
				get != null ? "/" : "", get != null ? get : "");
	}

	@Override
	protected String getAuthorization() {
		return String.format("Client-ID %s", Imgur.IMGUR_API_CLIENTID);
	}
}