package com.thatapplefreak.voxelcam.net.imgur;

public interface ImgurResponse<T> {

	T getData();

	int getStatus();

	boolean isSuccessful();

}
