package com.thatapplefreak.voxelcam.upload.twitter;

import java.io.File;

import com.google.common.base.Throwables;
import com.thatapplefreak.voxelcam.net.Callback;
import com.thatapplefreak.voxelcam.net.Poster;
import com.thatapplefreak.voxelcam.net.twitter.TwitterImage;
import com.thatapplefreak.voxelcam.net.twitter.TwitterImageResponse;
import com.thatapplefreak.voxelcam.net.twitter.TwitterStatus;
import com.thatapplefreak.voxelcam.net.twitter.TwitterStatusResponse;

public class TwitterHandler implements Runnable {

	private String text;
	private File file;
	private Callback<TwitterStatusResponse> callback;

	public static void doTwitter(String text, File screenshot, Callback<TwitterStatusResponse> screen) {
		new Thread(new TwitterHandler(text, screenshot, screen), "Twitter_Post_Thread").start();
	}

	private TwitterHandler(String text, File file, Callback<TwitterStatusResponse> callback) {
		this.text = text;
		this.file = file;
		this.callback = callback;
	}

	@Override
	public void run() {
		try {
			Poster.instance.post(new TwitterImage(file), new Callback<TwitterImageResponse>() {
				@Override
				public void onSuccess(TwitterImageResponse response) {
					onImageUpload(response);
				}

				@Override
				public void onFailure(Throwable t) {
					callback.onFailure(t);
				}
			});
		} catch (Exception e) {
			callback.onFailure(e);
		}
	}

	private void onImageUpload(TwitterImageResponse response) {
		String images = response.getMediaIdString();
		Poster.instance.post(new TwitterStatus(text + " #VoxelCam", images), new Callback<TwitterStatusResponse>() {
			@Override
			public void onSuccess(TwitterStatusResponse response) {
				callback.onSuccess(response);
			}

			@Override
			public void onFailure(Throwable t) {
				Throwables.propagate(t);
			}
		});
	}
}
