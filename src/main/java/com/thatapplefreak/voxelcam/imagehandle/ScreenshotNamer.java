package com.thatapplefreak.voxelcam.imagehandle;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotNamer {

	private SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
	private File screenshots;

	public ScreenshotNamer(File screenshotsDir) {
		this.screenshots = screenshotsDir;
	}

	public File getScreenshotFile() {

		String name = date.format(new Date());

		int i = 1;
		File file;
		do {
			file = new File(screenshots, name + (i == 1 ? "" : "_" + i) + ".png");
			i++;
		} while (file.exists());
		return file;
	}
}
