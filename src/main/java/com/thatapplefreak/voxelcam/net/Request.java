package com.thatapplefreak.voxelcam.net;

public interface Request<Response> {

	String getRequestUrl();

	Class<Response> getResponseClass();

	void onResponse(Response response);

}
