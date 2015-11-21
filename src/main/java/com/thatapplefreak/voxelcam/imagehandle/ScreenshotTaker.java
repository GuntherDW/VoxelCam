package com.thatapplefreak.voxelcam.imagehandle;

import static com.thatapplefreak.voxelcam.Translations.SAVED_SCREENSHOT_AS;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.thatapplefreak.voxelcam.VoxelCamCore;
import com.thatapplefreak.voxelcam.imagehandle.metadata.MetaDataHandler;
import com.thatapplefreak.voxelcam.upload.AutoUploader;
import com.voxelmodpack.common.util.ChatMessageBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;

public class ScreenshotTaker {

	protected int savepercent = 0;
	protected boolean isWritingToFile = false;
	private boolean screenshotIsSaving;
	private ScreenshotNamer namer;

	public ScreenshotTaker(File screenshotDir) {
		this.namer = new ScreenshotNamer(screenshotDir);
	}

	public synchronized void capture(int width, int height) {
		Minecraft mc = Minecraft.getMinecraft();
		if (OpenGlHelper.isFramebufferEnabled()) {
			width = mc.getFramebuffer().framebufferTextureWidth;
			height = mc.getFramebuffer().framebufferTextureHeight;
		}
		int totalPixels = width * height;

		IntBuffer pixelBuffer = BufferUtils.createIntBuffer(totalPixels);
		int[] pixelValues = new int[totalPixels];

		GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

		if (OpenGlHelper.isFramebufferEnabled()) {
			GlStateManager.bindTexture(mc.getFramebuffer().framebufferTexture);
			GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, pixelBuffer);
		} else {
			GL11.glReadPixels(0, 0, width, height, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, pixelBuffer);
		}

		pixelBuffer.get(pixelValues);
		TextureUtil.processPixelValues(pixelValues, width, height);

		save(pixelValues, width, height, namer.getScreenshotFile());
	}

	private void save(final int[] pixelValues, final int width, final int height, final File saveTo) {
		Thread imageSaveThread = new Thread("ImageSaver") {
			@Override
			public void run() {
				screenshotIsSaving = true;
				BufferedImage image;
				if (OpenGlHelper.isFramebufferEnabled()) {
					image = new BufferedImage(width, height, 1);

					double currentPixel = 0;
					double totalPixels = pixelValues.length;

					for (int h = 0; h < height; ++h) {
						for (int w = 0; w < width; ++w) {
							image.setRGB(w, h, pixelValues[h * width + w]);
							currentPixel++;
							savepercent = (int) ((currentPixel / totalPixels) * 100);
						}
					}

				} else {
					image = new BufferedImage(width, height, 1);
					savepercent = 100;
					image.setRGB(0, 0, width, height, pixelValues, 0, width);
				}

				try {
					isWritingToFile = true;
					ImageIO.write(image, "png", saveTo);
					MetaDataHandler.writeMetaData(saveTo);
					ChatMessageBuilder cmb = new ChatMessageBuilder();
					cmb.append("[VoxelCam]", EnumChatFormatting.DARK_RED, false);
					cmb.append(" " + I18n.format(SAVED_SCREENSHOT_AS) + ": ");
					cmb.append(saveTo.getName(), saveTo.getPath(), false);
					cmb.showChatMessageIngame();
					screenshotIsSaving = false;
					isWritingToFile = false;
					upload(saveTo);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		imageSaveThread.setPriority(5);
		imageSaveThread.start();
	}

	public boolean isWritingToFile() {
		return isWritingToFile;
	}

	public int getSavePercent() {
		return savepercent;
	}

	protected void upload(final File saveTo) {
		if (VoxelCamCore.getConfig().isAutoUpload()) {
			AutoUploader.upload(saveTo);
		}
	}

	public boolean isScreenshotSaving() {
		return screenshotIsSaving;
	}

}
