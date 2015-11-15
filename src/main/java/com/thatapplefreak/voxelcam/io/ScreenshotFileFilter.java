package com.thatapplefreak.voxelcam.io;

import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

public class ScreenshotFileFilter implements IOFileFilter {

	private String filter;

	public ScreenshotFileFilter(String filter) {
		this.filter = filter.toLowerCase();
	}

	@Override
	public boolean accept(File file) {
		String name = file.getName().toLowerCase();
		boolean ext = FilenameUtils.isExtension(name, "png");
		boolean match = filter.isEmpty() || name.matches(filter);
		return ext && match;
	}

	@Override
	public boolean accept(File dir, String name) {
		return false; // no directories!
	}

}
