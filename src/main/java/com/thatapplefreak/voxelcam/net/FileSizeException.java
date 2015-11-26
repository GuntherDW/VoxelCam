package com.thatapplefreak.voxelcam.net;

import java.io.IOException;

/**
 * Thrown when a file is too large.
 */
public class FileSizeException extends IOException {

	private static final long serialVersionUID = 1L;

	public FileSizeException(String msg) {
		super(msg);
	}
}
