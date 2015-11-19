package com.thatapplefreak.voxelcam.gui.upload;

import com.voxelmodpack.common.gui.GuiDialogBox;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class WorkingDialogPopup extends GuiDialogBox {

	public WorkingDialogPopup(GuiScreen parentScreen) {
		super(parentScreen, 210, 90, "Working");
	}

	@Override
	protected void onInitDialog() {
		super.onInitDialog();
		btnOk.visible = false;
	}

	@Override
	protected void drawDialog(int mouseX, int mouseY, float f) {
		super.drawDialog(mouseX, mouseY, f);
		drawCenteredString(fontRendererObj, I18n.format("working"), width / 2 - (150 / 2), height / 2 - 37, 0xFFFFFF);
	}

	@Override
	public void onSubmit() {
	}

	@Override
	public boolean validateDialog() {
		return false;
	}
}
