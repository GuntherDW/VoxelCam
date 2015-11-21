package com.thatapplefreak.voxelcam.net.auth;

public interface Authorizer {
	
	String HEADER = "Authorization";

	String getAuthorization();
}
