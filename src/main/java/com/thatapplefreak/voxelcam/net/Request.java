package com.thatapplefreak.voxelcam.net;

public interface Request<R> {

	String getRequestUrl();

	Class<R> getResponseClass();
	
	Method getMethod();

	void onResponse(Response<R> response);

	void onFailure(Throwable thrown);
}
