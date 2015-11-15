package com.thatapplefreak.voxelcam.gui.manager;

import java.io.File;
import java.text.SimpleDateFormat;

import com.thatapplefreak.voxelcam.io.VoxelCamIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;

public class PhotoSelector extends GuiTextSlot {

	private VoxelCamIO images;
	private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm aa");

	public PhotoSelector(GuiScreenShotManager parent, VoxelCamIO images, int listWidth) {
		super(listWidth, parent.height, 32, (parent.height - 55) + 4, 10, 35);
		this.images = images;
	}

	@Override
	protected int getContentHeight() {
		return (this.getSize()) * 35 + 1;
	}

	@Override
	protected int getSize() {
		return images.getScreenShotFiles().size();
	}

	@Override
	protected boolean isSelected(int i) {
		return images.isSelected(i);
	}

	@Override
	protected void drawBackground() {
	}

	@Override
	protected void drawSlot(int i, int j, int k, int l) {
		File pic = images.getScreenShotFiles().get(i);
		if (pic != null) {
			GlStateManager.enableBlend();
			FontRenderer font = Minecraft.getMinecraft().fontRendererObj;
			font.drawString(font.trimStringToWidth(pic.getName().replace(".png", ""), listWidth - 10), 13, k + 2, 0xFFFFFF);
			font.drawString(font.trimStringToWidth(sdf.format(pic.lastModified()), listWidth - 10), 13, k + 12, 0xCCCCCC);
		}
	}

	@Override
	protected void elementClicked(int i, boolean flag) {
		images.selectPhotoIndex(i);
	}

	public void setDimensionsAndPosition(int x, int y, int x2, int y2) {
		this.left = x;
		this.top = y;
		this.right = x2;
		this.bottom = y2;
		this.listWidth = x2 - x + 7;
	}
}