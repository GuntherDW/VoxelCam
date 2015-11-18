package com.thatapplefreak.voxelcam.net;

import org.apache.http.StatusLine;

public class Response<R> {

	private int statusCode;
	private String reasonPhrase;
	private R response;

	public Response(StatusLine status, R response) {
		this.statusCode = status.getStatusCode();
		this.reasonPhrase = status.getReasonPhrase();
		this.response = response;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getReasonPhrase() {
		return reasonPhrase;
	}

	public R getResponse() {
		return response;
	}
}
