package com.thatapplefreak.voxelcam.net;

public interface Callback<R> {

	void onSuccess(R response);

	void onFailure(Throwable t);

}
