package com.thatapplefreak.voxelcam.net.imgur;

public class ImgurUploadResponse implements ImgurResponse<ImageData> {

	private ImageData data;
	private boolean success;
	private int status;

	@Override
	public ImageData getData() {
		return data;
	}

	@Override
	public int getStatus() {
		return this.status;
	}

	@Override
	public boolean isSuccessful() {
		return this.success;
	}
}
