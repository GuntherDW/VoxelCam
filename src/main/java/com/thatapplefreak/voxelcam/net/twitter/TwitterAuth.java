package com.thatapplefreak.voxelcam.net.twitter;

import java.net.URL;

import com.thatapplefreak.voxelcam.net.OAuth2;
import com.thatapplefreak.voxelcam.upload.twitter.TwitterLoginPopup;
import com.thatapplefreak.voxelcam.upload.twitter.TwitterPINPopup;
import com.voxelmodpack.common.gui.GuiDialogBox;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public abstract class TwitterAuth implements OAuth2 {

	private static final String CONSUMER_KEY = "okIIDosE4TsrRP3JvXufw";
	private static final String CONSUMER_SECRET = "dFJIErDmYr61YwQfDdAGMAt79dCJGu1mpiflCAa2c";
	private static final String REQUEST_TOKEN_URL = "https://api.twitter.com/oauth/request_token";
	private static final String ACCESS_TOKEN_URL = "https://api.twitter.com/oauth/access_token";
	private static final String AUTHORIZE_URL = "https://api.twitter.com/oauth/authorize";

	@Override
	public String authorizeUser(URL auth) {
		Minecraft mc = Minecraft.getMinecraft();
		GuiScreen screen = mc.currentScreen;
		if (screen instanceof GuiDialogBox) screen = ((GuiDialogBox)screen).getParentScreen();
		mc.displayGuiScreen(new TwitterLoginPopup(screen, auth));
		while(!(mc.currentScreen instanceof TwitterPINPopup)
				|| !((TwitterPINPopup)mc.currentScreen).isReady()){
			// wait
		}
		TwitterPINPopup pin = (TwitterPINPopup) mc.currentScreen;
		mc.displayGuiScreen(screen);
		return pin.getPin();
	}

	@Override
	public String getConsumerKey() {
		return CONSUMER_KEY;
	}

	@Override
	public String getConsumerSecret() {
		return CONSUMER_SECRET;
	}

	@Override
	public String getAuthorizeUrl() {
		return AUTHORIZE_URL;
	}

	@Override
	public String getAccessTokenUrl() {
		return ACCESS_TOKEN_URL;
	}

	@Override
	public String getRequestTokenUrl() {
		return REQUEST_TOKEN_URL;
	}

	@Override
	public String getName() {
		return "Twitter";
	}

}
