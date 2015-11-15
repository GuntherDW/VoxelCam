package com.thatapplefreak.voxelcam.gui.manager;

import java.io.File;
import java.io.IOException;

import com.thatapplefreak.voxelcam.VoxelCamConfig;
import com.thatapplefreak.voxelcam.VoxelCamCore;
import com.thatapplefreak.voxelcam.imagehandle.ScreenshotIncapable;
import com.thatapplefreak.voxelcam.upload.CopyUploader;
import com.thatapplefreak.voxelcam.upload.CopyUploader.CopyResponse;
import com.thatapplefreak.voxelcam.upload.UploadCallback;
import com.thatapplefreak.voxelcam.upload.dropbox.DropboxHandler;
import com.thatapplefreak.voxelcam.upload.googleDrive.GoogleDriveHandler;
import com.thatapplefreak.voxelcam.upload.imgur.ImgurHandler;
import com.thatapplefreak.voxelcam.upload.imgur.ImgurUploadFailedPopup;
import com.thatapplefreak.voxelcam.upload.imgur.ImgurUploadResponse;
import com.thatapplefreak.voxelcam.upload.imgur.ImgurUploadSuccessPopup;
import com.thatapplefreak.voxelcam.upload.reddit.RedditHandler;
import com.thatapplefreak.voxelcam.upload.reddit.RedditLoginPopup;
import com.thatapplefreak.voxelcam.upload.reddit.RedditPostPopup;
import com.thatapplefreak.voxelcam.upload.twitter.TwitterLoginPopup;
import com.thatapplefreak.voxelcam.upload.twitter.TwitterPostPopup;
import com.voxelmodpack.common.gui.GuiDialogBox;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.Util;
import net.minecraft.util.Util.EnumOS;

public class PostPopup extends GuiDialogBox implements ScreenshotIncapable {

	GuiButton btnImgur, btnFacebook, btnTwitter, btnDropBox, btnGoogleDrive, btnReddit;

	private volatile GuiScreen completeDialog;
	private File toUpload;
	private boolean uploading = false;
	private ImgurHandler imgur;
	private DropboxHandler dropbox;
	private GoogleDriveHandler gDrive;

	public PostPopup(GuiScreen parentScreen, File toUpload) {
		super(parentScreen, 180, 120, I18n.format("postto") + "...");
		this.imgur = new ImgurHandler();
		this.dropbox = new DropboxHandler();
		this.gDrive = new GoogleDriveHandler();
		this.toUpload = toUpload;
	}

	@Override
	protected void onInitDialog() {
		buttonList.remove(btnOk);
		btnCancel.xPosition = dialogX + 60;

		buttonList.add(btnImgur = new GuiButton(0, dialogX + 15, dialogY + 40, 70, 20, "Imgur"));
		buttonList.add(btnFacebook = new GuiButton(2, dialogX + 95, dialogY + 10, 70, 20, "Facebook"));
		buttonList.add(btnTwitter = new GuiButton(3, dialogX + 15, dialogY + 10, 70, 20, "Twitter"));
		buttonList.add(btnReddit = new GuiButton(5, dialogX + 95, dialogY + 40, 70, 20, "Reddit"));

		btnDropBox = new GuiButton(1, dialogX + 15, dialogY + 70, 70, 20, "Dropbox");
		btnGoogleDrive = new GuiButton(4, dialogX + 95, dialogY + 70, 70, 20, "Google Drive");
		buttonList.add(btnDropBox);
		buttonList.add(btnGoogleDrive);

		if (!(new File(System.getProperty("user.home"), "/dropbox/").exists())) {
			btnDropBox.enabled = false;
		}
		if (!(new File(System.getProperty("user.home"), "/Google Drive/").exists())) {
			btnGoogleDrive.enabled = false;
		}

		btnFacebook.enabled = false;
	}

	@Override
	public void onSubmit() {
	}

	@Override
	public boolean validateDialog() {
		return true;
	}

	@Override
	protected void drawDialog(int mouseX, int mouseY, float f) {
		super.drawDialog(mouseX, mouseY, f);

		if (uploading) {
			buttonList.clear();
			drawCenteredString(fontRendererObj, I18n.format("uploading") + "...", width / 2, height / 2, 0xffffff);
		}

		if (this.completeDialog != null) {
			this.mc.displayGuiScreen(this.completeDialog);
			this.completeDialog = null;
		}
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (guibutton.equals(btnCancel)) {
			closeDialog();
		} else if (guibutton.equals(btnImgur)) {
			imgur.upload(toUpload, new UploadCallback<ImgurUploadResponse>() {
				@Override
				public void onCompleted(ImgurUploadResponse response) {
					ImgurUploadResponse uploadResponse = (ImgurUploadResponse) response;
					if (uploadResponse.isSuccessful()) {
						onUploadCompleted(new ImgurUploadSuccessPopup(getParentScreen(), uploadResponse.getDeleteHash(), uploadResponse.getLink()));
					} else {
						onUploadCompleted(new ImgurUploadFailedPopup(getParentScreen(), uploadResponse.get("data")));
					}
				}

				@Override
				public void onHTTPFailure(int responseCode, String responseMessage) {
					onUploadCompleted(new ImgurUploadFailedPopup(getParentScreen(), String.format("HTTP Error: %d %s", responseCode, responseMessage)));
				}
			});
			this.uploading = true;
		} else if (guibutton.equals(btnDropBox)) {
			dropbox.upload(toUpload, new FileCallback());
			mc.displayGuiScreen(getParentScreen());
		} else if (guibutton.equals(btnTwitter)) {
			if (VoxelCamCore.getConfig().getStringProperty(VoxelCamConfig.TWITTERAUTHTOKEN).equals("needLogin")) {
				mc.displayGuiScreen(new TwitterLoginPopup(getParentScreen(), toUpload));
			} else {
				mc.displayGuiScreen(new TwitterPostPopup(getParentScreen(), toUpload));
			}
		} else if (guibutton.equals(btnGoogleDrive)) {
			gDrive.upload(toUpload, new FileCallback());
			mc.displayGuiScreen(getParentScreen());
		} else if (guibutton.equals(btnReddit)) {
			if (!RedditHandler.isLoggedIn()) {
				mc.displayGuiScreen(new RedditLoginPopup(getParentScreen(), toUpload));
			} else {
				mc.displayGuiScreen(new RedditPostPopup(getParentScreen(), toUpload));
			}
		}
	}

	public void onUploadCompleted(GuiScreen g) {
		completeDialog = g;
	}

	private class FileCallback implements UploadCallback<CopyUploader.CopyResponse> {
		@Override
		public void onCompleted(CopyResponse response) {
			File file = response.getDestination();
			EnumOS os = Util.getOSType();
			try {
				if (os.equals(EnumOS.WINDOWS)) {
					new ProcessBuilder("explorer.exe", "/select,", file.toString()).start();
				} else if (os.equals(EnumOS.OSX)) {
					new ProcessBuilder("open", "-R", file.toString()).start();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		@Override
		public void onHTTPFailure(int responseCode, String responseMessage) {
			// ignore?
		}
	}

}
