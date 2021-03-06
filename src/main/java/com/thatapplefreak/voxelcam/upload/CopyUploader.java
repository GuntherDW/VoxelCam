package com.thatapplefreak.voxelcam.upload;

import java.io.File;
import java.io.IOException;

import com.google.common.io.Files;
import com.thatapplefreak.voxelcam.net.Callback;

public abstract class CopyUploader {

	/**
	 * Copies a file into the google drive folder /mcScreenshots/ and opens
	 * native file browser and highlights it
	 */
	public final void upload(File screenshot, Callback<CopyResponse> callback) {
		File destination = new File(getCopyDir(), "mcScreenshots/" + screenshot.getName());
		try {
			destination.getParentFile().mkdirs();
			Files.copy(screenshot, destination);
			callback.onSuccess(new CopyResponse(destination));
		} catch (IOException e) {
			e.printStackTrace();
			callback.onFailure(e);
		}
	}

	protected abstract File getCopyDir();

	public class CopyResponse {

		private File destination;

		public CopyResponse(File destination) {
			this.destination = destination;
		}

		public File getDestination() {
			return destination;
		}
	}
}
