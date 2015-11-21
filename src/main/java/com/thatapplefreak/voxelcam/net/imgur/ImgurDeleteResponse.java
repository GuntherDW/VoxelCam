package com.thatapplefreak.voxelcam.net.imgur;

public class ImgurDeleteResponse implements ImgurResponse<Boolean> {
	private boolean data;

	private boolean success;

	private int status;

	@Override
	public Boolean getData() {
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
