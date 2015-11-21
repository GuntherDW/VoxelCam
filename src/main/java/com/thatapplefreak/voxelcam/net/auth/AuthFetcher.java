package com.thatapplefreak.voxelcam.net.auth;

import java.net.URL;

import com.voxelmodpack.common.gui.GuiDialogBox;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public final class AuthFetcher {

	/**
	 * This should never be run in the client thread!
	 *
	 * @param url The URL to open
	 * @param auth The OAuth object to authenticate
	 * @return The pin that the user entered
	 */
	public static String getPin(URL url, OAuth2 auth) {
		GuiScreen screen = Minecraft.getMinecraft().currentScreen;
		if (screen instanceof GuiDialogBox)
			screen = ((GuiDialogBox) screen).getParentScreen();
		LoginPopup login = new LoginPopup(screen, url, auth);
		Minecraft.getMinecraft().displayGuiScreen(login);
		String pin;
		while ((pin = login.getPin()) == null) {
			// wait
		}
		return pin;
	}
}
