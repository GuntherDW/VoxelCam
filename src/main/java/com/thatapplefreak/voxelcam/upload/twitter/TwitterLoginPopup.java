package com.thatapplefreak.voxelcam.upload.twitter;

import java.net.URL;

import com.voxelmodpack.common.gui.GuiDialogBox;
import com.voxelmodpack.common.util.BrowserOpener;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class TwitterLoginPopup extends GuiDialogBox {

	private URL url;

	public TwitterLoginPopup(GuiScreen parentScreen, URL url) {
		super(parentScreen, 210, 90, "Log in to Twitter");
		this.url = url;
	}

	@Override
	protected void onInitDialog() {
		btnOk.displayString = "Ok";
		btnOk.visible = url != null;
	}

	@Override
	protected void drawDialog(int mouseX, int mouseY, float f) {
		super.drawDialog(mouseX, mouseY, f);
		if (url == null) {
			drawString(fontRendererObj, "You need to sign into Twitter.", width / 2 - 150 / 2, height / 2 - 37, 0xffffff);
			return;
		}
		drawString(fontRendererObj, I18n.format("twitauthline1"), width / 2 - (150 / 2), height / 2 - 37, 0xFFFFFF);
		drawString(fontRendererObj, I18n.format("twitauthline2"), width / 2 - (150 / 2) - 25, height / 2 - 27, 0xFFFFFF);
		drawString(fontRendererObj, I18n.format("twitauthline3"), width / 2 - (150 / 2) - 25, height / 2 - 17, 0xFFFFFF);
		drawString(fontRendererObj, I18n.format("twitauthline4"), width / 2 - (150 / 2) - 25, height / 2 - 7, 0xFFFFFF);
		drawString(fontRendererObj, I18n.format("twitauthline5"), width / 2 - (150 / 2) - 25, height / 2 + 3, 0xFFFFFF);
	}

	@Override
	public void onSubmit() {
	}

	@Override
	public boolean validateDialog() {
		mc.displayGuiScreen(new TwitterPINPopup(getParentScreen()));
		BrowserOpener.openURLinBrowser(url);
		return false;
	}

}
