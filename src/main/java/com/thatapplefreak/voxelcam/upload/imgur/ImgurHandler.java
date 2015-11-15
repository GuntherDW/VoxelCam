package com.thatapplefreak.voxelcam.upload.imgur;

import java.io.File;

import com.thatapplefreak.voxelcam.upload.IUploader;
import com.thatapplefreak.voxelcam.upload.UploadCallback;

public class ImgurHandler implements IUploader<ImgurUploadResponse> {

	public void upload(File screenshot, UploadCallback<ImgurUploadResponse> callback) {
		new ImgurUpload(screenshot).withTitle(screenshot.getName()).start(callback);
	}

}
