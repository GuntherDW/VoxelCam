package com.thatapplefreak.voxelcam.net.auth;

import static com.thatapplefreak.voxelcam.Translations.*;
import java.net.URL;

import com.voxelmodpack.common.gui.GuiDialogBox;
import com.voxelmodpack.common.util.BrowserOpener;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class LoginPopup extends GuiDialogBox {

	private URL url;
	private	OAuth2 auth;
	private AuthFetcher authFetcher;

	public LoginPopup(GuiScreen parentScreen, URL url, OAuth2 auth, AuthFetcher authFetcher) {
		super(parentScreen, 210, 90, I18n.format("login"));
		this.url = url;
		this.auth = auth;
		this.authFetcher = authFetcher;
	}

	@Override
	protected void onInitDialog() {
		btnOk.displayString = "Ok";
	}

	@Override
	protected void drawDialog(int mouseX, int mouseY, float f) {
		super.drawDialog(mouseX, mouseY, f);
		String auth = I18n.format(AUTH, this.auth.getName());
		fontRendererObj.drawSplitString(auth, width/2-150/2, height/2-37, dialogWidth, 0xFFFFFF);
	}

	@Override
	public void onSubmit() {
	}

	@Override
	public boolean validateDialog() {
		mc.displayGuiScreen(new PinPopup(this.getParentScreen(), authFetcher));
		BrowserOpener.openURLinBrowser(url);
		return false;
	}

}
