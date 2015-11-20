package com.thatapplefreak.voxelcam.net.imgur;

import com.thatapplefreak.voxelcam.net.Method;

public class ImgurDelete extends Imgur<ImgurDeleteResponse> {

	public ImgurDelete(String id) {
		super(id);
	}

	@Override
	public Class<ImgurDeleteResponse> getResponseClass() {
		return ImgurDeleteResponse.class;
	}

	@Override
	public Method getMethod() {
		return Method.DELETE;
	}
}
