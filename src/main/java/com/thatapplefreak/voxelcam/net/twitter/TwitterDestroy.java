package com.thatapplefreak.voxelcam.net.twitter;

import org.apache.http.client.methods.RequestBuilder;

import com.thatapplefreak.voxelcam.net.BasicPayload;
import com.thatapplefreak.voxelcam.net.Method;
import com.thatapplefreak.voxelcam.net.Request;

public class TwitterDestroy extends TwitterAuth implements Request<TwitterDestroyResponse>, BasicPayload {

	private static final String ENDPOINT = "https://api.twitter.com/1.1/statuses/destroy/%s.json";

	private String id;

	public TwitterDestroy(String id) {
		this.id = id;
	}

	@Override
	public void assemblePayload(RequestBuilder data) {
		data.addHeader("id", id);
		data.addHeader("trim_user", "true");
	}

	@Override
	public String getRequestUrl() {
		return String.format(ENDPOINT, id);
	}

	@Override
	public Class<TwitterDestroyResponse> getResponseClass() {
		return TwitterDestroyResponse.class;
	}

	@Override
	public Method getMethod() {
		return Method.POST;
	}

}
