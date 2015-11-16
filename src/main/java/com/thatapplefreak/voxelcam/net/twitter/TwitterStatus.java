package com.thatapplefreak.voxelcam.net.twitter;

import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.base.Joiner;
import com.thatapplefreak.voxelcam.net.BasicPayload;

public class TwitterStatus extends TwitterAuth implements BasicPayload<TwitterStatusResponse> {

	private static final String REQUEST_URL = "https://api.twitter.com/1.1/statuses/update.json";

	private static final int TWEET_MAX = 140;
	private static final int IMAGES_MAX = 4;

	private String tweet; // max 140 chars
	private String[] images; // max 4

	public TwitterStatus(String tweet, String... images) {
		if (tweet.length() > TWEET_MAX) {
			tweet = tweet.substring(0, 120);
		}
		if (images.length > IMAGES_MAX) {
			images = ArrayUtils.subarray(images, 0, 4);
		}
		this.tweet = tweet;
		this.images = images;
	}

	@Override
	public String getRequestUrl() {
		return REQUEST_URL;
	}

	@Override
	public void assemblePayload(Map<String, String> data) {
		data.put("status", this.tweet);
		if (this.images.length > 0) {
			data.put("media_ids", Joiner.on(',').join(images));
		}
	}

	@Override
	public Class<TwitterStatusResponse> getResponseClass() {
		return TwitterStatusResponse.class;
	}

	@Override
	public void onResponse(TwitterStatusResponse response) {
		// TODO Auto-generated method stub
		
	}
}
