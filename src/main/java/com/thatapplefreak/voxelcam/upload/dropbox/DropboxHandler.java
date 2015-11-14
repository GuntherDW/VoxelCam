package com.thatapplefreak.voxelcam.upload.dropbox;

import java.io.File;

import com.thatapplefreak.voxelcam.upload.CopyUploader;

public class DropboxHandler extends CopyUploader {

	public DropboxHandler(boolean open) {
		super(open);
	}

	@Override
	protected File getCopyDir() {
		return new File(System.getProperty("user.home") + "/dropbox");
	}

}
