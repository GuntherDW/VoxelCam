package com.thatapplefreak.voxelcam.upload.imgur;

import java.io.File;

import com.thatapplefreak.voxelcam.VoxelCamCore;
import com.thatapplefreak.voxelcam.net.Callback;
import com.thatapplefreak.voxelcam.net.Request;
import com.thatapplefreak.voxelcam.net.imgur.ImgurUpload;
import com.thatapplefreak.voxelcam.net.imgur.ImgurUploadResponse;

public class ImgurHandler {

	public void upload(File screenshot, Callback<ImgurUploadResponse> callback) {
		Request<?> req = new ImgurUpload(screenshot, callback);
		try {
			VoxelCamCore.instance().getImagePoster().post(req);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
