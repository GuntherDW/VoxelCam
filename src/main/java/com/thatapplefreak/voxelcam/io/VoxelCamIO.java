package com.thatapplefreak.voxelcam.io;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

import com.google.common.collect.ImmutableList;
import com.thatapplefreak.voxelcam.VoxelCamCore;
import com.thatapplefreak.voxelcam.imagehandle.GLImageMemoryHandler;

public class VoxelCamIO implements Comparator<File> {

	/**
	 * List of all PNG files in the screenshot directory
	 */
	private List<File> screenShotFiles;

	/**
	 * Index of the currently selected photo
	 */
	private int selected = 0;

	public VoxelCamIO() {
		updateScreenShotFilesList("");
	}
	
	public List<File> getScreenShotFiles() {
		return ImmutableList.copyOf(screenShotFiles);
	}

	public void selectPhotoIndex(int i) {
		selected = i;
	}

	public boolean isSelected(int i) {
		return i == selected;
	}

	public void updateScreenShotFilesList(String filter) {
		IOFileFilter fileFilter = new ScreenshotFileFilter(filter);
		Collection<File> collection = FileUtils.listFiles(VoxelCamCore.getScreenshotsDir(), fileFilter, null);
		List<File> filesInScreenshotDir = (List<File>) collection;
		Collections.sort(filesInScreenshotDir, this);

		this.screenShotFiles = filesInScreenshotDir;
	}

	public void rename(String string) {
		File newFile = new File(VoxelCamCore.getScreenshotsDir(), string + ".png");
		getSelectedPhoto().renameTo(newFile);
	}

	public void delete() {
		if (selected < 0 || selected >= screenShotFiles.size())
			return;
		GLImageMemoryHandler.requestImageRemovalFromMem(getSelectedPhoto());
		getSelectedPhoto().delete(); // EXTERMINATE!
		screenShotFiles.remove(selected); // remove refrence
		previous();
	}

	public File getSelectedPhoto() {
		return selected < 0 || selected >= screenShotFiles.size() ? null : screenShotFiles.get(selected);
	}

	@Override
	public int compare(File o1, File o2) {
		long modified = o2.lastModified() - o1.lastModified();
		return modified > 0 ? 1 : modified < 0 ? -1 : 0;
	}

	public void next() {
		if (selected < screenShotFiles.size() - 1)
			selectPhotoIndex(selected + 1);
	}

	public void previous() {
		if (selected > 0)
			selectPhotoIndex(selected - 1);
	}

}
