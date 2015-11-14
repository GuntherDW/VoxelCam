package com.thatapplefreak.voxelcam.upload;

import java.io.File;
import java.io.IOException;

import com.google.common.io.Files;

import net.minecraft.util.Util;
import net.minecraft.util.Util.EnumOS;

public abstract class CopyUploader implements IUploader {

	private boolean openFileManager;
	private File copy;
	
	public CopyUploader(boolean open) {
		this.openFileManager = open;
	}
	/**
	 * Copies a file into the google drive folder /mcScreenshots/ and opens
	 * native file browser and highlights it
	 */
	public final void upload(File screenshot) {
		this.copy = null;
		File destination = new File(getCopyDir(), "mcScreenshots/" + screenshot.getName());
		destination.getParentFile().mkdirs();
		try {
			Files.copy(screenshot, destination);
			if (openFileManager) {
				EnumOS os = Util.getOSType();
				if (os.equals(EnumOS.WINDOWS)) {
					new ProcessBuilder("explorer.exe", "/select,", destination.toString()).start();
				} else if (os.equals(EnumOS.OSX)) {
					new ProcessBuilder("open", "-R", destination.toString()).start();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.copy = destination;
	}

	protected abstract File getCopyDir();
	
	public File getCopy() {
		return copy;
	}
}
