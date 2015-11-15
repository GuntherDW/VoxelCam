package com.thatapplefreak.voxelcam.upload;

public interface UploadCallback<Response> {

	public abstract void onCompleted(Response response);

	public abstract void onHTTPFailure(int responseCode, String responseMessage);
}
