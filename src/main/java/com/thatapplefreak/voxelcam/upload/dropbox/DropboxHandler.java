package com.thatapplefreak.voxelcam.upload.dropbox;

import java.io.File;

import com.thatapplefreak.voxelcam.upload.CopyUploader;

public class DropboxHandler extends CopyUploader {

	@Override
	protected File getCopyDir() {
		return new File(System.getProperty("user.home") + "/dropbox");
	}

}
