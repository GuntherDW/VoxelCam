package com.thatapplefreak.voxelcam.upload;

import static com.thatapplefreak.voxelcam.Translations.AUTO_UPLOAD_FAIL;
import static com.thatapplefreak.voxelcam.Translations.AUTO_UPLOAD_SUCCESS;
import static com.thatapplefreak.voxelcam.Translations.CLICK_TO_VIEW;
import static com.thatapplefreak.voxelcam.Translations.NO_INSTALL_ERROR;

import java.io.File;

import com.thatapplefreak.voxelcam.VoxelCamConfig;
import com.thatapplefreak.voxelcam.VoxelCamCore;
import com.thatapplefreak.voxelcam.net.Callback;
import com.thatapplefreak.voxelcam.net.imgur.ImgurUpload;
import com.thatapplefreak.voxelcam.net.imgur.ImgurUploadResponse;
import com.thatapplefreak.voxelcam.upload.CopyUploader.CopyResponse;
import com.thatapplefreak.voxelcam.upload.dropbox.DropboxHandler;
import com.thatapplefreak.voxelcam.upload.googleDrive.GoogleDriveHandler;
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
				cmb.append(" " + I18n.format(NO_INSTALL_ERROR, "Dropbox"));
				cmb.showChatMessageIngame();
			}
		}
		if (config.isAutoUploadGoogleDrive()) {
			if (new File(System.getProperty("user.home"), "/Google Drive/").exists()) {
				uploadToGoogleDrive(image);
			} else {
				ChatMessageBuilder cmb = new ChatMessageBuilder();
				cmb.append("[VoxelCam]", EnumChatFormatting.DARK_RED, false);
				cmb.append(" " + I18n.format(NO_INSTALL_ERROR, "Google Drive"));
				cmb.showChatMessageIngame();
			}
		}
		if (config.isAutoUploadImgur()) {
			uploadToImgur(image);
		}
	}

	private static void uploadToImgur(File image) {
		VoxelCamCore.instance().getImagePoster().post(new ImgurUpload(image), new Callback<ImgurUploadResponse>() {
			@Override
			public void onSuccess(ImgurUploadResponse response) {
				if (response.isSuccessful()) {
					ChatMessageBuilder cmb = new ChatMessageBuilder();
					cmb.append("[VoxelCam]", EnumChatFormatting.DARK_RED, false);
					cmb.append(" Imgur: " + I18n.format(AUTO_UPLOAD_SUCCESS) + ": ");
					cmb.append(response.getData().getLink(), response.getData().getLink(), true);
					cmb.showChatMessageIngame();
				} else {
					ChatMessageBuilder cmb = new ChatMessageBuilder();
					cmb.append("[VoxelCam]", EnumChatFormatting.DARK_RED);
					cmb.append(" Imgur: " + I18n.format(AUTO_UPLOAD_FAIL, response.getData().getError()));
					cmb.showChatMessageIngame();
				}
			}

			@Override
			public void onFailure(Throwable t) {
				new ChatMessageBuilder()
				.append("[VoxelCam]", EnumChatFormatting.DARK_RED, false)
				.append(" Imgur: " + I18n.format(AUTO_UPLOAD_FAIL) + " ")
				.append(t.getMessage())
				.showChatMessageIngame();
			}
		});
	}

	private static void uploadToDropbox(File image) {
		new DropboxHandler().upload(image, new CopyCallback("Dropbox"));
	}

	private static void uploadToGoogleDrive(File image) {
		new GoogleDriveHandler().upload(image, new CopyCallback("Google Drive"));
	}

	private static class CopyCallback implements Callback<CopyUploader.CopyResponse> {

		private String to;

		private CopyCallback(String to) {
			this.to = to;
		}

		@Override
		public void onSuccess(CopyResponse response) {
			File file = response.getDestination();
			new ChatMessageBuilder()
			.append("[VoxelCam] ", EnumChatFormatting.DARK_RED)
			.append(to + ": " + I18n.format(AUTO_UPLOAD_SUCCESS) + " ") // grr, translatable
			.append(I18n.format(CLICK_TO_VIEW), file.getPath(), false)
			.showChatMessageIngame();
		}

		@Override
		public void onFailure(Throwable t) {
			new ChatMessageBuilder()
			.append("[VoxelCam] ", EnumChatFormatting.DARK_RED)
			.append(to + ": " + I18n.format(AUTO_UPLOAD_FAIL, t.getMessage()))
			.showChatMessageIngame();
		}
	}

}
