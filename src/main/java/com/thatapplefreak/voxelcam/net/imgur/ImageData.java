package com.thatapplefreak.voxelcam.net.imgur;

import com.thatapplefreak.voxelcam.net.Method;

public class ImageData {

	// normal
	private String id;
	private String deletehash;
	private String link;
	private String title;
	private String description;
	private int datetime;
	private String type;
	private boolean animated;
	private int width;
	private int height;
	private int size;
	private int views;
	private int bandwidth;
	private boolean favorite;
	private String nsfw;
	private String section;

	// errors
	private String error;
	private String request;
	private Method method;

	public int getBandwidth() {
		return bandwidth;
	}

	/**
	 * When uploading anonymously, this hash can be used to delete the image by
	 * passing the hash to a delete task
	 */
	public String getDeleteHash() {
		return this.deletehash;
	}

	public String getDescription() {
		return this.description;
	}

	public int getHeight() {
		return this.height;
	}

	/**
	 * imgur ID of the uploaded image
	 */
	public String getID() {
		return this.id;
	}

	/**
	 * URL to the uploaded image
	 */
	public String getLink() {
		return this.link;
	}

	/**
	 * MIME type, eg. image/jpeg
	 */
	public String getMimeType() {
		return this.type;
	}

	public String getNSFW() {
		return this.nsfw;
	}

	public String getSection() {
		return this.section;
	}

	public int getSize() {
		return this.size;
	}

	public int getTimestamp() {
		return this.datetime;
	}

	public String getTitle() {
		return this.title;
	}

	public int getViews() {
		return this.views;
	}

	public int getWidth() {
		return this.width;
	}

	public boolean isAnimated() {
		return this.animated;
	}

	public boolean isFavourite() {
		return this.favorite;
	}

	public String getError() {
		return this.error;
	}

	public String getRequest() {
		return this.request;
	}

	public Method getMethod() {
		return this.method;
	}
}