package com.thatapplefreak.voxelcam.upload.googleDrive;

import java.io.File;

import com.thatapplefreak.voxelcam.upload.CopyUploader;

public class GoogleDriveHandler extends CopyUploader {

	@Override
	protected File getCopyDir() {
		return new File(System.getProperty("user.home" + "/Google Drive"));
	}
}
