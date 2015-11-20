package com.thatapplefreak.voxelcam.upload.reddit;

import static com.thatapplefreak.voxelcam.Translations.COPY_LINK;
import static com.thatapplefreak.voxelcam.Translations.OPEN;
import static com.thatapplefreak.voxelcam.Translations.POST_SUCCESS;
import static com.thatapplefreak.voxelcam.Translations.REDDIT_POST_SUCCESS;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;

import com.thatapplefreak.voxelcam.imagehandle.ScreenshotIncapable;
import com.voxelmodpack.common.gui.GuiDialogBox;
import com.voxelmodpack.common.util.BrowserOpener;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class RedditPostSuccessPopup extends GuiDialogBox implements ScreenshotIncapable {
	
	private final String url;
	
	private GuiButton btnView, btnClipboard;

	public RedditPostSuccessPopup(GuiScreen parentScreen, String postUrl) {
		super(parentScreen, 365, 80, I18n.format(REDDIT_POST_SUCCESS));
		this.url = postUrl;
	}
	
	@Override
	protected void onInitDialog() {
		btnCancel.visible = false;
		btnCancel.enabled = false;
		btnView = new GuiButton(100, dialogX + dialogWidth - 186, dialogY + dialogHeight - 22, 60, 20, I18n.format(OPEN));
		buttonList.add(btnView);
		btnClipboard = new GuiButton(200, dialogX + dialogWidth - 124, dialogY + dialogHeight - 22, 60, 20, I18n.format(COPY_LINK));
		buttonList.add(btnClipboard);
	}
	
	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (guibutton.id == btnView.id) {
			BrowserOpener.openURLstringInBrowser(url);
		} else if (guibutton.id == btnClipboard.id) {
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(url), null);
		}

		super.actionPerformed(guibutton);
	}
	
	@Override
	protected void drawDialog(int mouseX, int mouseY, float f) {
		drawCenteredString(fontRendererObj, I18n.format(POST_SUCCESS), dialogX + (dialogWidth / 2), dialogY + 18, 0xFFFFAA00);
		drawCenteredString(fontRendererObj, this.url, dialogX + (dialogWidth / 2), dialogY + 32, 0xFFFFFF55);
	}

	@Override
	public void onSubmit() {
	}

	@Override
	public boolean validateDialog() {
		return true;
	}

}
