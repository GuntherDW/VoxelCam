package com.thatapplefreak.voxelcam.upload.imgur;

import java.io.File;

import com.thatapplefreak.voxelcam.VoxelCamCore;
import com.thatapplefreak.voxelcam.net.Callback;
import com.thatapplefreak.voxelcam.net.Request;
import com.thatapplefreak.voxelcam.net.imgur.ImgurUpload;
import com.thatapplefreak.voxelcam.net.imgur.ImgurUploadResponse;

public class ImgurHandler {

	public void upload(File screenshot, Callback<ImgurUploadResponse> callback) {
		Request<ImgurUploadResponse> req = new ImgurUpload(screenshot);
		VoxelCamCore.instance().getImagePoster().post(req, callback);
	}

}
