package com.thatapplefreak.voxelcam.net.auth;

import java.net.URL;

import com.voxelmodpack.common.gui.GuiDialogBox;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

/**
 * Class for fetching authorization from the user.
 */
public final class AuthFetcher {

	private volatile boolean waiting;
	private String pin;

	/**
	 * Opens a panel so the user can enter a PIN in order to allow us to use the
	 * account. It waits until the user submits it before returning.
	 *
	 * @param url The URL to open
	 * @param auth The OAuth object to authenticate
	 * @return The pin that the user entered
	 */
	public String getPin(URL url, OAuth2 auth) {
		GuiScreen screen = Minecraft.getMinecraft().currentScreen;
		if (screen instanceof GuiDialogBox)
			screen = ((GuiDialogBox) screen).getParentScreen();
		LoginPopup login = new LoginPopup(screen, url, auth, this);
		Minecraft.getMinecraft().displayGuiScreen(login);
		waiting = true;
		while (waiting) {}
		return pin;
	}

	/**
	 * Sets the pin and stops the ongoing loop.
	 * 
	 * @param pin The pin
	 */
	void setPin(String pin) {
		this.pin = pin;
		stop();
	}

	/**
	 * Stops the loop. Maybe the user cancelled?
	 */
	void stop() {
		waiting = false;
	}
}
