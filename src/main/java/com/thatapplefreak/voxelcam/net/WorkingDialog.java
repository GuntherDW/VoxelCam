package com.thatapplefreak.voxelcam.net;

import com.voxelmodpack.common.gui.GuiDialogBox;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class WorkingDialog extends GuiDialogBox {

	private Thread thread;

	public WorkingDialog(GuiScreen parentScreen, Thread thread) {
		super(parentScreen, 210, 90, "Working");
		this.thread = thread;
	}

	@Override
	protected void onInitDialog() {
		super.onInitDialog();
		btnOk.visible = false;
	}

	@Override
	protected void drawDialog(int mouseX, int mouseY, float f) {
		// TODO Auto-generated method stub
		super.drawDialog(mouseX, mouseY, f);
		drawCenteredString(fontRendererObj, I18n.format("working"), width / 2 - (150 / 2), height / 2 - 37, 0xFFFFFF);
	}

	@Override
	public void onSubmit() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean validateDialog() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void onDialogClosed() {
		if (thread.isAlive())
			thread.interrupt();
	}
}
