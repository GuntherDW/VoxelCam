package com.thatapplefreak.voxelcam.net.twitter;

import com.google.gson.annotations.SerializedName;

public class TwitterImageResponse {

	@SerializedName("media_id")
	private long mediaId;
	@SerializedName("media_id_string")
	private String mediaIdString;
	private int size;
	private Image image;

	public long getMediaId() {
		return mediaId;
	}

	public Image getImage() {
		return image;
	}

	public String getMediaIdString() {
		return mediaIdString;
	}

	public int getSize() {
		return size;
	}

	public class Image {
		private int w;
		private int h;
		@SerializedName("image_type")
		private String type;

		public int getWidth() {
			return w;
		}

		public int getHeight() {
			return h;
		}

		public String getType() {
			return type;
		}
	}
}
