package com.thatapplefreak.voxelcam.gui.upload;

import static com.thatapplefreak.voxelcam.Translations.*;
import com.voxelmodpack.common.gui.GuiDialogBox;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;


public class UploadFailedPopup extends GuiDialogBox {
	
	protected String errorMessage;

	public UploadFailedPopup(GuiScreen parentScreen, String errorMessage) {
		super(parentScreen, 300, 80, I18n.format(UPLOAD_FAIL));
		this.errorMessage = errorMessage;
	}

	@Override
	protected void onInitDialog() {
		btnOk.displayString = I18n.format(CLOSE);
		btnCancel.enabled = false;
	}

	@Override
	public void onSubmit() {
	}

	@Override
	public boolean validateDialog() {
		return true;
	}

	@Override
	protected void drawDialog(int mouseX, int mouseY, float f) {
		drawCenteredString(fontRendererObj, I18n.format(UPLOAD_FAIL), dialogX + (dialogWidth / 2), dialogY + 18, 0xFFFF5555);
		drawCenteredString(fontRendererObj, this.errorMessage, dialogX + (dialogWidth / 2), dialogY + 32, 0xFFFFAA00);
	}
}
