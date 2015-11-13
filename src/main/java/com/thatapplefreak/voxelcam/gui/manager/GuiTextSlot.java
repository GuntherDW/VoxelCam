package com.thatapplefreak.voxelcam.gui.manager;

import static org.lwjgl.opengl.GL11.GL_ALPHA_TEST;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CLIP_PLANE0;
import static org.lwjgl.opengl.GL11.GL_CLIP_PLANE1;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FLAT;
import static org.lwjgl.opengl.GL11.GL_FOG;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClipPlane;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glShadeModel;

import java.nio.DoubleBuffer;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;


@SuppressWarnings({ "rawtypes", "cast" })
public abstract class GuiTextSlot {
	protected int listWidth;
	protected int listHeight;
	protected int top;
	protected int bottom;
	protected int right;
	protected int left;
	protected int slotHeight;
	protected int scrollUpActionId;
	protected int scrollDownActionId;
	protected int mouseX;
	protected int mouseY;
	protected float initialMouseClickY = -2.0F;
	protected float scrollFactor;
	protected float scrollDistance;
	protected int selectedIndex = -1;
	protected long lastClickTime = 0L;
	protected boolean field_25123_p = true;
	protected boolean field_27262_q;
	protected int field_27261_r;
	protected DoubleBuffer doubleBuffer;

	public GuiTextSlot(int width, int height, int top, int bottom, int left, int entryHeight) {
		this.listWidth = width;
		this.listHeight = height;
		this.top = top;
		this.bottom = bottom;
		this.slotHeight = entryHeight;
		this.left = left;
		this.right = width + this.left;
	}

	public void func_27258_a(boolean p_27258_1_) {
		this.field_25123_p = p_27258_1_;
	}

	protected void func_27259_a(boolean p_27259_1_, int p_27259_2_) {
		this.field_27262_q = p_27259_1_;
		this.field_27261_r = p_27259_2_;

		if (!p_27259_1_) {
			this.field_27261_r = 0;
		}
	}

	protected abstract int getSize();

	protected abstract void elementClicked(int index, boolean doubleClick);

	protected abstract boolean isSelected(int index);

	protected int getContentHeight() {
		return this.getSize() * this.slotHeight + this.field_27261_r;
	}

	protected abstract void drawBackground();

	protected abstract void drawSlot(int var1, int var2, int var3, int var4, Tessellator var5);

	protected void func_27260_a(int p_27260_1_, int p_27260_2_, Tessellator p_27260_3_) {
	}

	protected void func_27255_a(int p_27255_1_, int p_27255_2_) {
	}

	protected void func_27257_b(int p_27257_1_, int p_27257_2_) {
	}

	public int func_27256_c(int p_27256_1_, int p_27256_2_)

	{
		int var3 = this.left + 1;
		int var4 = this.left + this.listWidth - 7;
		int var5 = p_27256_2_ - this.top - this.field_27261_r + (int) this.scrollDistance - 4;
		int var6 = var5 / this.slotHeight;
		return p_27256_1_ >= var3 && p_27256_1_ <= var4 && var6 >= 0 && var5 >= 0 && var6 < this.getSize() ? var6 : -1;
	}

	public void registerScrollButtons(List p_22240_1_, int p_22240_2_, int p_22240_3_) {
		this.scrollUpActionId = p_22240_2_;
		this.scrollDownActionId = p_22240_3_;
	}

	protected void applyScrollLimits() {
		int var1 = this.getContentHeight() - (this.bottom - this.top - 4);

		if (var1 < 0) {
			var1 /= 2;
		}

		if (this.scrollDistance < 0.0F) {
			this.scrollDistance = 0.0F;
		}

		if (this.scrollDistance > var1) {
			this.scrollDistance = var1;
		}
	}

	public void actionPerformed(GuiButton button) {
		if (button.enabled) {
			if (button.id == this.scrollUpActionId) {
				this.scrollDistance -= this.slotHeight * 2 / 3;
				this.initialMouseClickY = -2.0F;
				this.applyScrollLimits();
			} else if (button.id == this.scrollDownActionId) {
				this.scrollDistance += this.slotHeight * 2 / 3;
				this.initialMouseClickY = -2.0F;
				this.applyScrollLimits();
			}
		}
	}

	public void drawScreen(int mouseX, int mouseY, float p_22243_3_) {
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		this.drawBackground();
		int listLength = this.getSize();
		int scrollBarXStart = this.left + this.listWidth - 6;
		int scrollBarXEnd = scrollBarXStart + 6;
		int boxLeft = this.left;
		int boxRight = scrollBarXStart - 1;
		int var10;
		int var11;
		int var13;
		int var19;

		if (Mouse.isButtonDown(0)) {
			if (this.initialMouseClickY == -1.0F) {
				boolean var7 = true;

				if (mouseY >= this.top && mouseY <= this.bottom) {
					var10 = mouseY - this.top - this.field_27261_r + (int) this.scrollDistance - 4;
					var11 = var10 / this.slotHeight;

					if (mouseX >= boxLeft && mouseX <= boxRight && var11 >= 0 && var10 >= 0 && var11 < listLength) {
						boolean var12 = var11 == this.selectedIndex && System.currentTimeMillis() - this.lastClickTime < 250L;
						this.elementClicked(var11, var12);
						this.selectedIndex = var11;
						this.lastClickTime = System.currentTimeMillis();
					} else if (mouseX >= boxLeft && mouseX <= boxRight && var10 < 0) {
						this.func_27255_a(mouseX - boxLeft, mouseY - this.top + (int) this.scrollDistance - 4);
						var7 = false;
					}

					if (mouseX >= scrollBarXStart && mouseX <= scrollBarXEnd) {
						this.scrollFactor = -1.0F;
						var19 = this.getContentHeight() - (this.bottom - this.top - 4);

						if (var19 < 1) {
							var19 = 1;
						}

						var13 = (int) ((float) ((this.bottom - this.top) * (this.bottom - this.top)) / (float) this.getContentHeight());

						if (var13 < 32) {
							var13 = 32;
						}

						if (var13 > this.bottom - this.top - 8) {
							var13 = this.bottom - this.top - 8;
						}

						this.scrollFactor /= (float) (this.bottom - this.top - var13) / (float) var19;
					} else {
						this.scrollFactor = 1.0F;
					}

					if (var7) {
						this.initialMouseClickY = mouseY;
					} else {
						this.initialMouseClickY = -2.0F;
					}
				} else {
					this.initialMouseClickY = -2.0F;
				}
			} else if (this.initialMouseClickY >= 0.0F) {
				this.scrollDistance -= (mouseY - this.initialMouseClickY) * this.scrollFactor;
				this.initialMouseClickY = mouseY;
			}
		} else {
			while (Mouse.next()) {
				int var16 = Mouse.getEventDWheel();

				if (var16 != 0) {
					if (var16 > 0) {
						var16 = -1;
					} else if (var16 < 0) {
						var16 = 1;
					}

					this.scrollDistance += var16 * this.slotHeight / 2;
				}
			}

			this.initialMouseClickY = -1.0F;
		}

		this.applyScrollLimits();
		this.enableClipping(this.top, this.bottom);

		glDisable(GL_LIGHTING);
		glDisable(GL_FOG);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer wr = tessellator.getWorldRenderer();
		// glBindTexture(GL_TEXTURE_2D,
		// this.client.renderEngine.getTexture("/gui/background.png"));
		glEnable(GL_BLEND);
		glDisable(GL_TEXTURE_2D);
		glColor4f(0.0F, 0.0F, 0.0F, 0.5F);
		float var17 = 32.0F;
		wr.startDrawingQuads();
		// var18.setColorOpaque_I(2105376);
		wr.addVertexWithUV(this.left, this.bottom, 0.0D, this.left / var17, (this.bottom + (int) this.scrollDistance) / var17);
		wr.addVertexWithUV(this.right, this.bottom, 0.0D, this.right / var17, (this.bottom + (int) this.scrollDistance) / var17);
		wr.addVertexWithUV(this.right, this.top, 0.0D, this.right / var17, (this.top + (int) this.scrollDistance) / var17);
		wr.addVertexWithUV(this.left, this.top, 0.0D, this.left / var17, (this.top + (int) this.scrollDistance) / var17);
		tessellator.draw();
		glDisable(GL_BLEND);
		glEnable(GL_TEXTURE_2D);
		var10 = this.top + 4 - (int) this.scrollDistance;

		if (this.field_27262_q) {
			this.func_27260_a(boxRight, var10, tessellator);
		}

		int var14;

		for (var11 = 0; var11 < listLength; ++var11) {
			var19 = var10 + var11 * this.slotHeight + this.field_27261_r;
			var13 = this.slotHeight - 4;

			if (var19 <= this.bottom && var19 + var13 >= this.top) {
				if (this.field_25123_p && this.isSelected(var11)) {
					var14 = boxLeft;
					int var15 = boxRight;
					glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					glDisable(GL_TEXTURE_2D);
					wr.startDrawingQuads();
					wr.setColorOpaque_I(8421504);
					wr.addVertexWithUV(var14, var19 + var13 + 2, 0.0D, 0.0D, 1.0D);
					wr.addVertexWithUV(var15, var19 + var13 + 2, 0.0D, 1.0D, 1.0D);
					wr.addVertexWithUV(var15, var19 - 2, 0.0D, 1.0D, 0.0D);
					wr.addVertexWithUV(var14, var19 - 2, 0.0D, 0.0D, 0.0D);
					wr.setColorOpaque_I(0);
					wr.addVertexWithUV(var14 + 1, var19 + var13 + 1, 0.0D, 0.0D, 1.0D);
					wr.addVertexWithUV(var15 - 1, var19 + var13 + 1, 0.0D, 1.0D, 1.0D);
					wr.addVertexWithUV(var15 - 1, var19 - 1, 0.0D, 1.0D, 0.0D);
					wr.addVertexWithUV(var14 + 1, var19 - 1, 0.0D, 0.0D, 0.0D);
					tessellator.draw();
					glEnable(GL_TEXTURE_2D);
				}

				this.drawSlot(var11, boxRight, var19, var13, tessellator);
			}
		}

		glDisable(GL_DEPTH_TEST);
		byte var20 = 4;
		// this.overlayBackground(0, this.top, 255, 255);
		// this.overlayBackground(this.bottom, this.listHeight, 255, 255);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glDisable(GL_ALPHA_TEST);
		glShadeModel(GL_SMOOTH);
		glDisable(GL_TEXTURE_2D);
		wr.startDrawingQuads();
		wr.setColorRGBA_I(0, 0);
		wr.addVertexWithUV(this.left, this.top + var20, 0.0D, 0.0D, 1.0D);
		wr.addVertexWithUV(this.right, this.top + var20, 0.0D, 1.0D, 1.0D);
		wr.setColorRGBA_I(0, 255);
		wr.addVertexWithUV(this.right, this.top, 0.0D, 1.0D, 0.0D);
		wr.addVertexWithUV(this.left, this.top, 0.0D, 0.0D, 0.0D);
		tessellator.draw();
		wr.startDrawingQuads();
		wr.setColorRGBA_I(0, 255);
		wr.addVertexWithUV(this.left, this.bottom, 0.0D, 0.0D, 1.0D);
		wr.addVertexWithUV(this.right, this.bottom, 0.0D, 1.0D, 1.0D);
		wr.setColorRGBA_I(0, 0);
		wr.addVertexWithUV(this.right, this.bottom - var20, 0.0D, 1.0D, 0.0D);
		wr.addVertexWithUV(this.left, this.bottom - var20, 0.0D, 0.0D, 0.0D);
		tessellator.draw();
		var19 = this.getContentHeight() - (this.bottom - this.top - 4);

		if (var19 > 0) {
			var13 = (this.bottom - this.top) * (this.bottom - this.top) / this.getContentHeight();

			if (var13 < 32) {
				var13 = 32;
			}

			if (var13 > this.bottom - this.top - 8) {
				var13 = this.bottom - this.top - 8;
			}

			var14 = (int) this.scrollDistance * (this.bottom - this.top - var13) / var19 + this.top;

			if (var14 < this.top) {
				var14 = this.top;
			}

			wr.startDrawingQuads();
			wr.setColorRGBA_I(0, 255);
			wr.addVertexWithUV(scrollBarXStart, this.bottom, 0.0D, 0.0D, 1.0D);
			wr.addVertexWithUV(scrollBarXEnd, this.bottom, 0.0D, 1.0D, 1.0D);
			wr.addVertexWithUV(scrollBarXEnd, this.top, 0.0D, 1.0D, 0.0D);
			wr.addVertexWithUV(scrollBarXStart, this.top, 0.0D, 0.0D, 0.0D);
			tessellator.draw();
			wr.startDrawingQuads();
			wr.setColorRGBA_I(8421504, 255);
			wr.addVertexWithUV(scrollBarXStart, var14 + var13, 0.0D, 0.0D, 1.0D);
			wr.addVertexWithUV(scrollBarXEnd, var14 + var13, 0.0D, 1.0D, 1.0D);
			wr.addVertexWithUV(scrollBarXEnd, var14, 0.0D, 1.0D, 0.0D);
			wr.addVertexWithUV(scrollBarXStart, var14, 0.0D, 0.0D, 0.0D);
			tessellator.draw();
			wr.startDrawingQuads();
			wr.setColorRGBA_I(12632256, 255);
			wr.addVertexWithUV(scrollBarXStart, var14 + var13 - 1, 0.0D, 0.0D, 1.0D);
			wr.addVertexWithUV(scrollBarXEnd - 1, var14 + var13 - 1, 0.0D, 1.0D, 1.0D);
			wr.addVertexWithUV(scrollBarXEnd - 1, var14, 0.0D, 1.0D, 0.0D);
			wr.addVertexWithUV(scrollBarXStart, var14, 0.0D, 0.0D, 0.0D);
			tessellator.draw();
		}

		this.func_27257_b(mouseX, mouseY);
		glEnable(GL_TEXTURE_2D);
		glShadeModel(GL_FLAT);
		glEnable(GL_ALPHA_TEST);
		glDisable(GL_BLEND);

		this.disableClipping();
	}

	// protected void overlayBackground(int p_22239_1_, int p_22239_2_, int
	// p_22239_3_, int p_22239_4_)
	// {
	// Tessellator var5 = Tessellator.instance;
	// glBindTexture(GL_TEXTURE_2D,
	// this.client.renderEngine.getTexture("/gui/background.png"));
	// glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	// float var6 = 32.0F;
	// var5.startDrawingQuads();
	// var5.setColorRGBA_I(4210752, p_22239_4_);
	// var5.addVertexWithUV(0.0D, (double)p_22239_2_, 0.0D, 0.0D,
	// (double)((float)p_22239_2_ / var6));
	// var5.addVertexWithUV((double)this.listWidth + 30, (double)p_22239_2_,
	// 0.0D, (double)((float)(this.listWidth + 30) / var6),
	// (double)((float)p_22239_2_ / var6));
	// var5.setColorRGBA_I(4210752, p_22239_3_);
	// var5.addVertexWithUV((double)this.listWidth + 30, (double)p_22239_1_,
	// 0.0D, (double)((float)(this.listWidth + 30) / var6),
	// (double)((float)p_22239_1_ / var6));
	// var5.addVertexWithUV(0.0D, (double)p_22239_1_, 0.0D, 0.0D,
	// (double)((float)p_22239_1_ / var6));
	// var5.draw();
	// }

	protected final void enableClipping(int yTop, int yBottom) {
		if (doubleBuffer == null) {
			doubleBuffer = BufferUtils.createByteBuffer(32).asDoubleBuffer();
		}

		doubleBuffer.clear();
		doubleBuffer.put(0).put(1).put(0).put(-yTop).flip();
		glClipPlane(GL_CLIP_PLANE0, doubleBuffer);

		doubleBuffer.clear();
		doubleBuffer.put(0).put(-1).put(0).put(yBottom).flip();
		glClipPlane(GL_CLIP_PLANE1, doubleBuffer);

		glEnable(GL_CLIP_PLANE0);
		glEnable(GL_CLIP_PLANE1);
	}

	protected final void disableClipping() {
		glDisable(GL_CLIP_PLANE1);
		glDisable(GL_CLIP_PLANE0);
	}
}
