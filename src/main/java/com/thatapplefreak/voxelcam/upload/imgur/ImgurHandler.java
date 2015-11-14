package com.thatapplefreak.voxelcam.upload.imgur;

import java.io.File;

import com.thatapplefreak.voxelcam.upload.IUploader;

public class ImgurHandler implements IUploader {

	private ImgurCallback callback;

	public ImgurHandler(ImgurCallback callback) {
		this.callback = callback;
	}

	public void upload(File screenshot) {
		final ImgurUpload poster = new ImgurUpload(screenshot, screenshot.getName(), "");
		poster.start(callback);
	}

}
