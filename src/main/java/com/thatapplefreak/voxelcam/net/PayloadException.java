package com.thatapplefreak.voxelcam.net;

public class PayloadException extends Exception {

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
