package com.thatapplefreak.voxelcam.net;

import org.apache.http.entity.mime.MultipartEntityBuilder;

public interface MultipartPayload extends Payload {

	void assemblePayload(MultipartEntityBuilder builder) throws PayloadException;
}
