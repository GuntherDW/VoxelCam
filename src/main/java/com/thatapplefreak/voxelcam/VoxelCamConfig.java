package com.thatapplefreak.voxelcam;

import org.lwjgl.input.Keyboard;

import com.voxelmodpack.common.properties.ModConfig;

import net.minecraft.client.settings.KeyBinding;

public class VoxelCamConfig extends ModConfig {

	// Keybinds that VoxelCam uses
	public static final KeyBinding KEY_OPENSCREENSHOTMANAGER = new KeyBinding("ScreenShot Manager", Keyboard.KEY_H, "VoxelCam");

	// Strings to access the settings
	public static final String FIRSTRUN = "firstRun";

	public static final String PHOTOWIDTH = "photoWidth";
	public static final String PHOTOHEIGHT = "photoHeight";

	public static final String AUTO_UPLOAD = "autoUplaod";
	public static final String AUTO_UPLOAD_IMGUR = "autoUploadToImgur";
	public static final String AUTO_UPLOAD_DROPBOX = "autoUploadToDropbox";
	public static final String AUTO_UPLOAD_GOOGLEDRIVE = "autoUploadToGoogleDrive";

	public VoxelCamConfig() {
		super("VoxelCam", "voxelcam.properties");
	}

	/**
	 * Sets the default values for the settings
	 */
	@Override
	protected void setDefaults() {
		defaults.put(FIRSTRUN, "true");
		defaults.put(PHOTOWIDTH, "1920");
		defaults.put(PHOTOHEIGHT, "1080");
		defaults.put(AUTO_UPLOAD, "false");
		defaults.put(AUTO_UPLOAD_IMGUR, "false");
		defaults.put(AUTO_UPLOAD_DROPBOX, "false");
		defaults.put(AUTO_UPLOAD_GOOGLEDRIVE, "false");
	}

	public boolean isFirstRun() {
		return getBoolProperty(FIRSTRUN);
	}

	public boolean isAutoUpload() {
		return getBoolProperty(AUTO_UPLOAD);
	}

	public boolean isAutoUploadImgur() {
		return getBoolProperty(AUTO_UPLOAD_IMGUR);
	}

	public boolean isAutoUploadDropbox() {
		return getBoolProperty(AUTO_UPLOAD_DROPBOX);
	}

	public boolean isAutoUploadGoogleDrive() {
		return getBoolProperty(AUTO_UPLOAD_GOOGLEDRIVE);
	}

	public int getPhotoWidth() {
		return getIntProperty(PHOTOWIDTH);
	}

	public int getPhotoHeight() {
		return getIntProperty(PHOTOHEIGHT);
	}
}
