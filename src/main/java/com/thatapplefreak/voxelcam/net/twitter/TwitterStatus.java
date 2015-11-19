package com.thatapplefreak.voxelcam.net.twitter;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.client.methods.RequestBuilder;

import com.google.common.base.Joiner;
import com.thatapplefreak.voxelcam.gui.upload.UploadFailedPopup;
import com.thatapplefreak.voxelcam.net.BasicPayload;
import com.thatapplefreak.voxelcam.net.Callback;
import com.thatapplefreak.voxelcam.net.Method;
import com.thatapplefreak.voxelcam.net.Request;
import com.thatapplefreak.voxelcam.net.Response;

import net.minecraft.client.Minecraft;

public class TwitterStatus extends TwitterAuth implements Request<TwitterStatusResponse>, BasicPayload {

	private static final String REQUEST_URL = "https://api.twitter.com/1.1/statuses/update.json";

	private static final int TWEET_MAX = 140;
	private static final int IMAGES_MAX = 4;

	private String tweet; // max 140 chars
	private String[] imageIds; // max 4
	private Callback<TwitterStatusResponse> callback;

	public TwitterStatus(String tweet, String[] images, Callback<TwitterStatusResponse> callback) {
		if (tweet.length() > TWEET_MAX) {
			tweet = tweet.substring(0, 120);
		}
		if (images.length > IMAGES_MAX) {
			images = ArrayUtils.subarray(images, 0, 4);
		}
		this.tweet = tweet;
		this.imageIds = images;
		this.callback = callback;
	}

	@Override
	public void assemblePayload(RequestBuilder data) {
		data.addParameter("status", this.tweet);
		if (this.imageIds.length > 0) {
			data.addParameter("media_ids", Joiner.on(',').join(imageIds));
		}
	}

	@Override
	public Class<TwitterStatusResponse> getResponseClass() {
		return TwitterStatusResponse.class;
	}

	@Override
	public void onResponse(Response<TwitterStatusResponse> response) {
		if (callback != null)
			callback.onCompleted(response.getResponse());
	}

	@Override
	public void onFailure(Throwable thrown) {
		Minecraft mc = Minecraft.getMinecraft();
		mc.displayGuiScreen(new UploadFailedPopup(mc.currentScreen, thrown.getMessage()));
	}

	@Override
	public String getRequestUrl() {
		return REQUEST_URL;
	}

	@Override
	public Method getMethod() {
		return Method.POST;
	}
}
