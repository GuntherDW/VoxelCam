package com.thatapplefreak.voxelcam.net;

public interface Request<R> {

	String getRequestUrl();

	Class<R> getResponseClass();
	
	Method getMethod();

}
