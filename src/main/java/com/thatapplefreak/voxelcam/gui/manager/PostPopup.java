package com.thatapplefreak.voxelcam.gui.manager;

import static com.thatapplefreak.voxelcam.Translations.POST_TO;
import static com.thatapplefreak.voxelcam.Translations.UPLOADING;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import com.thatapplefreak.voxelcam.gui.upload.UploadFailedPopup;
import com.thatapplefreak.voxelcam.gui.upload.UploadSuccessPopup;
import com.thatapplefreak.voxelcam.gui.upload.WorkingDialogPopup;
import com.thatapplefreak.voxelcam.imagehandle.ScreenshotIncapable;
import com.thatapplefreak.voxelcam.net.Callback;
import com.thatapplefreak.voxelcam.net.Poster;
import com.thatapplefreak.voxelcam.net.imgur.ImgurDelete;
import com.thatapplefreak.voxelcam.net.imgur.ImgurUploadResponse;
import com.thatapplefreak.voxelcam.net.twitter.TwitterAuth;
import com.thatapplefreak.voxelcam.upload.CopyUploader;
import com.thatapplefreak.voxelcam.upload.CopyUploader.CopyResponse;
import com.thatapplefreak.voxelcam.upload.dropbox.DropboxHandler;
import com.thatapplefreak.voxelcam.upload.googleDrive.GoogleDriveHandler;
import com.thatapplefreak.voxelcam.upload.imgur.ImgurHandler;
import com.thatapplefreak.voxelcam.upload.reddit.RedditHandler;
import com.thatapplefreak.voxelcam.upload.twitter.TwitterPostPopup;
import com.voxelmodpack.common.gui.GuiDialogBox;
import com.voxelmodpack.common.util.BrowserOpener;

import net.minecraft.client.Minecraft;
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
	private DropboxHandler dropbox;
	private GoogleDriveHandler gDrive;

	public PostPopup(GuiScreen parentScreen, File toUpload) {
		super(parentScreen, 180, 120, I18n.format(POST_TO) + "...");
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
		//btnReddit.enabled = false;
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
			drawCenteredString(fontRendererObj, I18n.format(UPLOADING) + "...", width / 2, height / 2, 0xffffff);
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
			ImgurHandler.doImgur(toUpload, new Callback<ImgurUploadResponse>() {
				@Override
				public void onSuccess(ImgurUploadResponse response) {
					if (response.isSuccessful()) {
						String delete = response.getData().getDeleteHash();
						String link = response.getData().getLink();
						onUploadCompleted(new UploadSuccessPopup(getParentScreen(), link, new ImgurDelete(delete)));
					} else {
						onUploadCompleted(new UploadFailedPopup(getParentScreen(), response.getData().getError()));
					}
				}

				@Override
				public void onFailure(Throwable t) {
					onUploadCompleted(new UploadFailedPopup(getParentScreen(), t.getMessage()));
				}
			});
			this.uploading = true;
		} else if (guibutton.equals(btnDropBox)) {
			dropbox.upload(toUpload, new FileCallback());
			mc.displayGuiScreen(getParentScreen());
		} else if (guibutton.equals(btnTwitter)) {
			TwitterPostPopup twitter = new TwitterPostPopup(getParentScreen(), toUpload);
			if (!Poster.instance.isLoggedIn("Twitter")) {
				// try to log in
				Poster.instance.authenticate(new TwitterAuth());
				mc.displayGuiScreen(new WorkingDialogPopup(twitter));
			} else {
				mc.displayGuiScreen(twitter);
			}
		} else if (guibutton.equals(btnGoogleDrive)) {
			gDrive.upload(toUpload, new FileCallback());
			mc.displayGuiScreen(getParentScreen());
		} else if (guibutton.equals(btnReddit)) {
			RedditHandler.doRedditPost(toUpload, new Callback<URI>() {
				@Override
				public void onSuccess(URI link) {
					BrowserOpener.openURIinBrowser(link);
					closeDialog();
				}

				@Override
				public void onFailure(Throwable t) {
					onUploadCompleted(new UploadFailedPopup(getParentScreen(), t.getMessage()));
				}
			});
			uploading = true;
		}
	}

	public void onUploadCompleted(GuiScreen g) {
		completeDialog = g;
	}

	private class FileCallback implements Callback<CopyUploader.CopyResponse> {
		@Override
		public void onSuccess(CopyResponse response) {
			File file = response.getDestination();
			EnumOS os = Util.getOSType();
			try {
				switch (os) {
				case WINDOWS:
					new ProcessBuilder("explorer.exe", "/select,", file.toString()).start();
					break;
				case OSX:
					new ProcessBuilder("open", "-R", file.toString()).start();
					break;
				default:
					// most linux platforms have this
					// doesn't support file selecting >.<
					new ProcessBuilder("xdg-open", file.getParent()).start();
					// Nautilus does, but not everyone has it
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable t) {
			Minecraft mc = Minecraft.getMinecraft();
			mc.displayGuiScreen(new UploadFailedPopup(mc.currentScreen, t.getMessage()));
		}
	}

}
