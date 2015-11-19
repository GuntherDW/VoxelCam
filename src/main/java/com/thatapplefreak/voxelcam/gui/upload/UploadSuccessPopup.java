package com.thatapplefreak.voxelcam.gui.upload;

import com.thatapplefreak.voxelcam.VoxelCamCore;
import com.thatapplefreak.voxelcam.net.Request;
import com.voxelmodpack.common.gui.GuiDialogBox;
import com.voxelmodpack.common.util.BrowserOpener;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class UploadSuccessPopup  extends GuiDialogBox {
	
	private final String url;
	private Request<?> undo;
	private GuiButton btnView, btnClipboard, btnUndo;

	public UploadSuccessPopup(GuiScreen parentScreen, String url, Request<?> undo) {
		super(parentScreen, 300, 80, I18n.format("imguruploadsuccess"));
		this.url = url;
		this.undo = undo;
	}
	
	@Override
	protected void onInitDialog() {
		btnCancel.visible = false;
		btnView = new GuiButton(100, dialogX + dialogWidth - 248, dialogY + dialogHeight - 22, 60, 20, I18n.format("open"));
		buttonList.add(btnView);
		btnClipboard = new GuiButton(200, dialogX + dialogWidth - 186, dialogY + dialogHeight - 22, 60, 20, I18n.format("copylink"));
		buttonList.add(btnClipboard);
		btnUndo = new GuiButton(300, btnCancel.xPosition, btnCancel.yPosition, 60, 20, I18n.format("undo"));
		buttonList.add(btnUndo);
	}
	
	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (guibutton.id == btnUndo.id) {
			try {
				VoxelCamCore.instance().getImagePoster().post(undo);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			closeDialog();
		} else if (guibutton.id == btnView.id) {
			BrowserOpener.openURLstringInBrowser(url);
		} else if (guibutton.id == btnClipboard.id) {
			GuiScreen.setClipboardString(url);
		}

		super.actionPerformed(guibutton);
	}
	
	@Override
	protected void drawDialog(int mouseX, int mouseY, float f) {
		drawCenteredString(fontRendererObj, I18n.format("uploadsuccess"), dialogX + (dialogWidth / 2), dialogY + 18, 0xFFFFAA00);
		drawCenteredString(fontRendererObj, this.url, dialogX + (dialogWidth / 2), dialogY + 32, 0xFFFFFF55);
	}

	@Override
	public boolean validateDialog() {
		return true;
	}

	@Override
	public void onSubmit() {
	}

}
