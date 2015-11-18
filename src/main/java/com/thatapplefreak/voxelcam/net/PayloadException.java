package com.thatapplefreak.voxelcam.net;

import java.io.IOException;

public class PayloadException extends IOException {

	private static final long serialVersionUID = 1L;

	public PayloadException() {
		super();
	}

	public PayloadException(String msg) {
		super(msg);
	}

	public PayloadException(Throwable cause) {
		super(cause);
	}

	public PayloadException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
