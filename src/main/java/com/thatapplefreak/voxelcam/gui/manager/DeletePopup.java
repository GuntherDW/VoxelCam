package com.thatapplefreak.voxelcam.gui.manager;
import static com.thatapplefreak.voxelcam.Translations.*;
import com.thatapplefreak.voxelcam.imagehandle.ScreenshotIncapable;
import com.thatapplefreak.voxelcam.io.VoxelCamIO;
import com.voxelmodpack.common.gui.GuiDialogBox;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class DeletePopup extends GuiDialogBox implements ScreenshotIncapable {

	private VoxelCamIO images;

	public DeletePopup(GuiScreen parentScreen, VoxelCamIO images) {
		super(parentScreen, 200, 75, I18n.format(DELETE));
		this.images = images;
	}

	@Override
	public void onSubmit() {
		images.delete();
	}

	@Override
	protected void onInitDialog() {
		this.btnOk.displayString = I18n.format(YES);
		this.btnCancel.displayString = I18n.format(NO);

	}

	@Override
	protected void onKeyTyped(char keyChar, int keyCode) {
		if (keyChar == 'y') {
			actionPerformed(btnOk);
		} else if (keyChar == 'n') {
			actionPerformed(btnCancel);
		}
	}

	@Override
	public boolean validateDialog() {
		return true;
	}

	@Override
	protected void drawDialog(int mouseX, int mouseY, float f) {
		super.drawDialog(mouseX, mouseY, f);
		drawCenteredString(fontRendererObj, I18n.format(ARE_YOU_SURE) + "?", width / 2, height / 2 - 12, 0xffffff);
	}

}
