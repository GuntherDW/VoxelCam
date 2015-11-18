package com.thatapplefreak.voxelcam.upload.twitter;

import java.io.File;

import com.thatapplefreak.voxelcam.VoxelCamCore;
import com.thatapplefreak.voxelcam.net.Callback;
import com.thatapplefreak.voxelcam.net.Poster;
import com.thatapplefreak.voxelcam.net.twitter.TwitterImage;
import com.thatapplefreak.voxelcam.net.twitter.TwitterImageResponse;
import com.thatapplefreak.voxelcam.net.twitter.TwitterStatus;

public class TwitterHandler {

	public static final String CONSUMER_KEY = "okIIDosE4TsrRP3JvXufw";
	public static final String CONSUMER_SECRET = "dFJIErDmYr61YwQfDdAGMAt79dCJGu1mpiflCAa2c";

	private String text;
//	private TwitterPostPopup callbackGui;

	public TwitterHandler(TwitterPostPopup callback) {
//		this.callbackGui = callback;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void doTwitter(final File screenshot) {
		new Thread("Twitter_Post_Thread") {
			@Override
			public void run() {
				try {
					final Poster poster = VoxelCamCore.instance().getImagePoster();
					TwitterImage image = new TwitterImage(screenshot, new Callback<TwitterImageResponse>() {

						@Override
						public void onCompleted(TwitterImageResponse response) {
							TwitterStatus status = new TwitterStatus(text + " #VoxelCam", response.getMediaIdString());
							try {
								poster.post(status);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
					poster.post(image);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
}
