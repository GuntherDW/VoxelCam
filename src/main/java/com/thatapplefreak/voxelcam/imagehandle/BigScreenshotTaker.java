package com.thatapplefreak.voxelcam.imagehandle;

import com.thatapplefreak.voxelcam.VoxelCamConfig;
import com.thatapplefreak.voxelcam.VoxelCamCore;
import com.voxelmodpack.common.gl.FBO;
import com.voxelmodpack.common.runtime.PrivateMethods;

import net.minecraft.client.Minecraft;

/**
 * Takes a big screenshot
 * 
 * @author thatapplefreak
 * 
 */
public abstract class BigScreenshotTaker {

	/**
	 * The original width of minecraft
	 */
	private static int originalWidthOfScreen;
	
	/**
	 * The original height of minecraft
	 */
	private static int originalHeightOfScreen;
	
	/**
	 * Waiting for minecraft to render to take a screenshot
	 */
	private static boolean waiting;

	/**
	 * The FrameBuffer that the big screenshot gets rendered to
	 */
	private static FBO fbo;

	public static void run() {
		Minecraft.getMinecraft().gameSettings.hideGUI = true;
		originalWidthOfScreen = Minecraft.getMinecraft().displayWidth;
		originalHeightOfScreen = Minecraft.getMinecraft().displayHeight;
		resizeMinecraft(VoxelCamCore.getConfig().getIntProperty(VoxelCamConfig.PHOTOWIDTH), VoxelCamCore.getConfig().getIntProperty(VoxelCamConfig.PHOTOHEIGHT));
		fbo = new FBO();
		fbo.begin(VoxelCamCore.getConfig().getIntProperty(VoxelCamConfig.PHOTOWIDTH), VoxelCamCore.getConfig().getIntProperty(VoxelCamConfig.PHOTOHEIGHT));
		waiting = true;
	}

	/**
	 * Sets minecraft to a custom size
	 */
	private static void resizeMinecraft(final int width, final int height) {
		PrivateMethods.resizeMinecraft.invokeVoid(Minecraft.getMinecraft(), width, height);
	}

	/**
	 * Returns Minecraft to it's original width and height
	 */
	private static void returnMinecraftToNormal() {
		PrivateMethods.resizeMinecraft.invokeVoid(Minecraft.getMinecraft(), originalWidthOfScreen, originalHeightOfScreen);
	}

	public static void onTick() {
		if (waiting) {
			ScreenshotTaker.capture(VoxelCamCore.getConfig().getIntProperty(VoxelCamConfig.PHOTOWIDTH), VoxelCamCore.getConfig().getIntProperty(VoxelCamConfig.PHOTOHEIGHT));
			fbo.end();
			fbo.dispose();
			returnMinecraftToNormal();
			Minecraft.getMinecraft().gameSettings.hideGUI = false;
			waiting = false;
		}
	}

}
