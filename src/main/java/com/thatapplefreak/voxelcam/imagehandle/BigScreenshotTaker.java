package com.thatapplefreak.voxelcam.imagehandle;

import java.lang.reflect.Method;

import com.mumfrey.liteloader.util.ObfuscationUtilities;
import com.voxelmodpack.common.gl.FBO;

import net.minecraft.client.Minecraft;

/**
 * Takes a big screenshot
 * 
 * @author thatapplefreak
 * 
 */
public class BigScreenshotTaker {

	private final Minecraft minecraft;
	private final int width;
	private final int height;

	/**
	 * The original width of minecraft
	 */
	private int originalWidthOfScreen;

	/**
	 * The original height of minecraft
	 */
	private int originalHeightOfScreen;

	/**
	 * The FrameBuffer that the big screenshot gets rendered to
	 */
	private FBO fbo;

	public BigScreenshotTaker(Minecraft mc, int width, int height) {
		this.minecraft = mc;
		this.width = width;
		this.height = height;

		minecraft.gameSettings.hideGUI = true;
		originalWidthOfScreen = minecraft.displayWidth;
		originalHeightOfScreen = minecraft.displayHeight;

		resizeMinecraft(width, height);
		fbo = new FBO();
		fbo.begin(width, height);
	}

	/**
	 * Sets minecraft to a custom size
	 */
	private void resizeMinecraft(final int width, final int height) {
		try {
			String method = ObfuscationUtilities.getObfuscatedFieldName("resize", "a", "func_71370_a");
			Method m = Minecraft.class.getDeclaredMethod(method, int.class, int.class);
			if (!m.isAccessible())
				m.setAccessible(true);
			m.invoke(Minecraft.getMinecraft(), width, height);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns Minecraft to it's original width and height
	 */
	private void returnMinecraftToNormal() {
		resizeMinecraft(originalWidthOfScreen, originalHeightOfScreen);
	}

	public void onTick(ScreenshotTaker screenshot) {
		screenshot.capture(width, height);
		fbo.end();
		fbo.dispose();
		returnMinecraftToNormal();
		Minecraft.getMinecraft().gameSettings.hideGUI = false;
	}

}
