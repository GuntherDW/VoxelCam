package com.thatapplefreak.voxelcam.upload.twitter;

import java.io.File;

import com.google.common.base.Throwables;
import com.thatapplefreak.voxelcam.VoxelCamCore;
import com.thatapplefreak.voxelcam.gui.upload.UploadFailedPopup;
import com.thatapplefreak.voxelcam.gui.upload.UploadSuccessPopup;
import com.thatapplefreak.voxelcam.net.Callback;
import com.thatapplefreak.voxelcam.net.Poster;
import com.thatapplefreak.voxelcam.net.Request;
import com.thatapplefreak.voxelcam.net.twitter.TwitterDestroy;
import com.thatapplefreak.voxelcam.net.twitter.TwitterImage;
import com.thatapplefreak.voxelcam.net.twitter.TwitterImageResponse;
import com.thatapplefreak.voxelcam.net.twitter.TwitterStatus;
import com.thatapplefreak.voxelcam.net.twitter.TwitterStatusResponse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class TwitterHandler implements Runnable {

	private static final Poster POSTER = VoxelCamCore.instance().getImagePoster();

	private String text;
	private File file;
	private GuiScreen parent;

	public static void doTwitter(String text, File screenshot, GuiScreen screen) {
		new Thread(new TwitterHandler(text, screenshot, screen), "Twitter_Post_Thread").start();
	}

	private TwitterHandler(String text, File file, GuiScreen parent) {
		this.text = text;
		this.file = file;
		this.parent = parent;
	}

	@Override
	public void run() {
		try {
			POSTER.post(new TwitterImage(file, new Callback<TwitterImageResponse>() {
				@Override
				public void onCompleted(TwitterImageResponse response) {
					onImageUpload(response);
				}
			}));
		} catch (Exception e) {
			Minecraft.getMinecraft().displayGuiScreen(new UploadFailedPopup(parent, e.getMessage()));
			e.printStackTrace();
		}
	}

	private void onImageUpload(TwitterImageResponse response) {
		try {
			String[] images = new String[] { response.getMediaIdString() };
			POSTER.post(new TwitterStatus(text + " #VoxelCam", images, new Callback<TwitterStatusResponse>() {
				@Override
				public void onCompleted(TwitterStatusResponse response) {
					onStatusUpdate(response);
				}
			}));
		} catch (Exception e) {
			Throwables.propagate(e);
		}
	}

	private void onStatusUpdate(TwitterStatusResponse response) {
		Request<?> undo = new TwitterDestroy(response.getId());
		Minecraft.getMinecraft().displayGuiScreen(new UploadSuccessPopup(parent, response.getUrl(), undo));
	}
}
