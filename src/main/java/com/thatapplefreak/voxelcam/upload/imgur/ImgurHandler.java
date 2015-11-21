package com.thatapplefreak.voxelcam.upload.imgur;

import java.io.File;

import com.thatapplefreak.voxelcam.net.Callback;
import com.thatapplefreak.voxelcam.net.Poster;
import com.thatapplefreak.voxelcam.net.imgur.ImgurUpload;
import com.thatapplefreak.voxelcam.net.imgur.ImgurUploadResponse;

public class ImgurHandler implements Runnable {

	private File file;
	private Callback<ImgurUploadResponse> callback;

	private ImgurHandler(File file, Callback<ImgurUploadResponse> callback) {
		this.file = file;
		this.callback = callback;
	}

	public static void doImgur(File file, Callback<ImgurUploadResponse> callback) {
		new Thread(new ImgurHandler(file, callback), "Imgur Upload Thread").start();
	}

	@Override
	public void run() {
		Poster.instance.post(new ImgurUpload(file), callback);
	}
}
