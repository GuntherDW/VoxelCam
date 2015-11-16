package com.thatapplefreak.voxelcam.net;

import java.util.Map;

public interface BasicPayload<Response> extends Payload {

	void assemblePayload(Map<String, String> data);
}
