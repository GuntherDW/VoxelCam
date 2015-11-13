package com.thatapplefreak.voxelcam.gui.mainmenu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class PhotoButton extends GuiButton {
	
	static ResourceLocation photoBtnPic = new ResourceLocation("voxelcam", "textures/photo.png");

	public PhotoButton(int xPos, int yPos) {
		super(111195, xPos, yPos, 20, 20, "");
		
	}
	
	@Override
	public void drawButton(Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_) {
		super.drawButton(p_146112_1_, p_146112_2_, p_146112_3_);
		Minecraft.getMinecraft().getTextureManager().bindTexture(photoBtnPic);
		GlStateManager.color(1, 1, 1, 1);
		drawTexturedModalRect(this.xPosition + 2, this.yPosition + 1, 0, 0, this.width, this.height);
	}
}