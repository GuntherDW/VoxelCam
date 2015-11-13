package com.thatapplefreak.voxelcam.imagehandle;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;

/**
 * Utility Class that can take any File that is an image and draw it to a gui
 * 
 * @author thatapplefreak
 */
public abstract class ImageDrawer {

	/**
	 * Draws the image given the top left and bottom right corners
	 * 
	 * @param img
	 *            GL integer name of the texture
	 * @param x
	 *            x coordinate of the top left corner
	 * @param y
	 *            y coordinate of the top left corner
	 * @param x2
	 *            x coordinate of the bottom right corner
	 * @param y2
	 *            y coordinate of the bottom right corner
	 */
	public static void drawImageToGui(int img, int x, int y, int x2, int y2) {
		GlStateManager.bindTexture(img);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		Tessellator t = Tessellator.getInstance();
		WorldRenderer wr = t.getWorldRenderer();
		wr.startDrawingQuads();
		wr.addVertexWithUV(x, y2, 0, 0, 1);
		wr.addVertexWithUV(x2, y2, 0, 1, 1);
		wr.addVertexWithUV(x2, y, 0, 1, 0);
		wr.addVertexWithUV(x, y, 0, 0, 0);
		t.draw();
		
		
	}
}
