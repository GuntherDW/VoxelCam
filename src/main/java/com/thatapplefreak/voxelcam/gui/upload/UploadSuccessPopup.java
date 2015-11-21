package com.thatapplefreak.voxelcam.gui.upload;

import static com.thatapplefreak.voxelcam.Translations.COPY_LINK;
import static com.thatapplefreak.voxelcam.Translations.OPEN;
import static com.thatapplefreak.voxelcam.Translations.UNDO;
import static com.thatapplefreak.voxelcam.Translations.UPLOAD_SUCCESS;

import com.thatapplefreak.voxelcam.net.Poster;
import com.thatapplefreak.voxelcam.net.Request;
import com.voxelmodpack.common.gui.GuiDialogBox;
import com.voxelmodpack.common.util.BrowserOpener;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class UploadSuccessPopup extends GuiDialogBox implements Runnable {
	
	private final String url;
	private Request<?> undo;
	private GuiButton btnView, btnClipboard, btnUndo;

	public UploadSuccessPopup(GuiScreen parentScreen, String url, Request<?> undo) {
		super(parentScreen, 300, 80, I18n.format(UPLOAD_SUCCESS));
		this.url = url;
		this.undo = undo;
	}
	
	@Override
	protected void onInitDialog() {
		btnCancel.visible = false;
		btnView = new GuiButton(100, dialogX + dialogWidth - 248, dialogY + dialogHeight - 22, 60, 20, I18n.format(OPEN));
		buttonList.add(btnView);
		btnClipboard = new GuiButton(200, dialogX + dialogWidth - 186, dialogY + dialogHeight - 22, 60, 20, I18n.format(COPY_LINK));
		buttonList.add(btnClipboard);
		btnUndo = new GuiButton(300, btnCancel.xPosition, btnCancel.yPosition, 60, 20, I18n.format(UNDO));
		buttonList.add(btnUndo);
	}
	
	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (guibutton.id == btnUndo.id) {
			new Thread(this, "Delete Thread").start();
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

	@Override
	public void run() {
		Poster.instance.post(undo, null);
	}

}
