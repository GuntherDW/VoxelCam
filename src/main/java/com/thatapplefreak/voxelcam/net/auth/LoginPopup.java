package com.thatapplefreak.voxelcam.net.auth;

import java.net.URL;

import com.voxelmodpack.common.gui.GuiDialogBox;
import com.voxelmodpack.common.util.BrowserOpener;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class LoginPopup extends GuiDialogBox {

	private URL url;
	private	OAuth2 auth;
	private PinPopup pin;

	public LoginPopup(GuiScreen parentScreen, URL url, OAuth2 auth) {
		super(parentScreen, 210, 90, I18n.format("login"));
		this.url = url;
		this.auth = auth;
	}

	@Override
	protected void onInitDialog() {
		btnOk.displayString = "Ok";
	}

	@Override
	protected void drawDialog(int mouseX, int mouseY, float f) {
		super.drawDialog(mouseX, mouseY, f);
		String auth = I18n.format("auth", this.auth.getName());
		fontRendererObj.drawSplitString(auth, width/2-150/2, height/2-37, width, 0xFFFFFF);
//		drawString(fontRendererObj, I18n.format("twitauthline1"), width / 2 - (150 / 2), height / 2 - 37, 0xFFFFFF);
//		drawString(fontRendererObj, I18n.format("twitauthline2"), width / 2 - (150 / 2) - 25, height / 2 - 27, 0xFFFFFF);
//		drawString(fontRendererObj, I18n.format("twitauthline3"), width / 2 - (150 / 2) - 25, height / 2 - 17, 0xFFFFFF);
//		drawString(fontRendererObj, I18n.format("twitauthline4"), width / 2 - (150 / 2) - 25, height / 2 - 7, 0xFFFFFF);
//		drawString(fontRendererObj, I18n.format("twitauthline5"), width / 2 - (150 / 2) - 25, height / 2 + 3, 0xFFFFFF);
	}

	@Override
	public void onSubmit() {
	}

	@Override
	public boolean validateDialog() {
		mc.displayGuiScreen(new PinPopup(this));
		BrowserOpener.openURLinBrowser(url);
		return false;
	}
	
	String getPin() {
		return pin == null ? null : pin.getPin();
	}

}
