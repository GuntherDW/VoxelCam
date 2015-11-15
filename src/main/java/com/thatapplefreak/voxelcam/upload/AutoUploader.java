package com.thatapplefreak.voxelcam.upload;

import java.io.File;

import com.thatapplefreak.voxelcam.VoxelCamConfig;
import com.thatapplefreak.voxelcam.VoxelCamCore;
import com.thatapplefreak.voxelcam.upload.CopyUploader.CopyResponse;
import com.thatapplefreak.voxelcam.upload.dropbox.DropboxHandler;
import com.thatapplefreak.voxelcam.upload.googleDrive.GoogleDriveHandler;
import com.thatapplefreak.voxelcam.upload.imgur.ImgurUpload;
import com.thatapplefreak.voxelcam.upload.imgur.ImgurUploadResponse;
import com.voxelmodpack.common.util.ChatMessageBuilder;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;

public abstract class AutoUploader {

	public static void upload(File image) {
		VoxelCamConfig config = VoxelCamCore.getConfig();
		if (config.isAutoUploadDropbox()) {
			if (new File(System.getProperty("user.home"), "/dropbox/").exists()) {
				uploadToDropbox(image);
			} else {
				ChatMessageBuilder cmb = new ChatMessageBuilder();
				cmb.append("[VoxelCam]", EnumChatFormatting.DARK_RED, false);
				cmb.append(" " + I18n.format("dropboxnoinstallerror"));
				cmb.showChatMessageIngame();
			}
		}
		if (config.isAutoUploadGoogleDrive()) {
			if (new File(System.getProperty("user.home"), "/Google Drive/").exists()) {
				uploadToGoogleDrive(image);
			} else {
				ChatMessageBuilder cmb = new ChatMessageBuilder();
				cmb.append("[VoxelCam]", EnumChatFormatting.DARK_RED, false);
				cmb.append(" " + I18n.format("googledrivenoinstallerror"));
				cmb.showChatMessageIngame();
			}
		}
		if (config.isAutoUploadImgur()) {
			uploadToImgur(image);
		}
	}

	private static void uploadToImgur(File image) {
		new ImgurUpload(image).withTitle(image.getName()).start(new UploadCallback<ImgurUploadResponse>() {

			@Override
			public void onHTTPFailure(int responseCode, String responseMessage) {
				ChatMessageBuilder cmb = new ChatMessageBuilder();
				cmb.append("[VoxelCam]", EnumChatFormatting.DARK_RED, false);
				cmb.append(" " + I18n.format("imgurautouploaderror") + " (");
				cmb.append(String.valueOf(responseCode));
				cmb.append("): ");
				cmb.append(responseMessage);
				cmb.showChatMessageIngame();
			}

			@Override
			public void onCompleted(ImgurUploadResponse response) {
				ChatMessageBuilder cmb = new ChatMessageBuilder();
				cmb.append("[VoxelCam]", EnumChatFormatting.DARK_RED, false);
				cmb.append(" " + I18n.format("imgurautouploadsuccess") + ": ");
				cmb.append(response.getLink(), response.getLink(), true);
				cmb.showChatMessageIngame();
			}
		});
	}

	private static void uploadToDropbox(File image) {
		new DropboxHandler().upload(image, new UploadCallback<CopyUploader.CopyResponse>() {
			@Override
			public void onCompleted(CopyResponse response) {
				File dropbox = response.getDestination();
				ChatMessageBuilder cmb = new ChatMessageBuilder();
				cmb.append("[VoxelCam]", EnumChatFormatting.DARK_RED, false);
				cmb.append(" " + I18n.format("dropboxautouploadsuccess") + " ");
				cmb.append(I18n.format("clicktoview"), dropbox.getPath(), false);
				cmb.showChatMessageIngame();
			}

			@Override
			public void onHTTPFailure(int responseCode, String responseMessage) {
			}

		});
	}

	private static void uploadToGoogleDrive(File image) {
		new GoogleDriveHandler().upload(image, new UploadCallback<CopyUploader.CopyResponse>() {
			@Override
			public void onCompleted(CopyResponse response) {
				File googleDrive = response.getDestination();
				ChatMessageBuilder cmb = new ChatMessageBuilder();
				cmb.append("[VoxelCam]", EnumChatFormatting.DARK_RED, false);
				cmb.append(" " + I18n.format("googledriveautouploadsuccess") + " ");
				cmb.append(I18n.format("clicktoview"), googleDrive.getPath(), false);
				cmb.showChatMessageIngame();
			}

			@Override
			public void onHTTPFailure(int responseCode, String responseMessage) {

			}

		});
	}

}
