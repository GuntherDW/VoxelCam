package com.thatapplefreak.voxelcam.net;

public interface Payload<Data> {
	void assemblePayload(Data data) throws PayloadException;
}
