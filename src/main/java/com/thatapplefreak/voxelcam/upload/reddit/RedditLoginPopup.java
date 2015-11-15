package com.thatapplefreak.voxelcam.upload.reddit;

import java.io.File;
import java.io.IOException;

import com.thatapplefreak.voxelcam.VoxelCamConfig;
import com.thatapplefreak.voxelcam.VoxelCamCore;
import com.thatapplefreak.voxelcam.imagehandle.ScreenshotIncapable;
import com.voxelmodpack.common.gui.GuiDialogBox;
import com.voxelmodpack.common.gui.GuiTextFieldEx;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class RedditLoginPopup extends GuiDialogBox implements ILoginCallback, ScreenshotIncapable {
	
	private boolean failed = false;
	private boolean loggingIn = false;
	private File toPost;
	private GuiTextFieldEx usernameField,passwordField;
	private VoxelCamConfig config;

	public RedditLoginPopup(GuiScreen parentScreen, File toPost) {
		super(parentScreen, 200, 100, I18n.format("pleaseloginto") + " Reddit"); //TODO Translate
		this.config = VoxelCamCore.getConfig();
		this.toPost = toPost;
	}
	
	@Override
	protected void onInitDialog() {
		super.onInitDialog();
		btnOk.displayString = I18n.format("login"); //TODO Traslate
		usernameField = new GuiTextFieldEx(0, fontRendererObj, dialogX + 65, dialogY + 18, 130, 15, config.getRedditUsername());
		passwordField = new GuiTextFieldEx(1, fontRendererObj, dialogX + 65, dialogY + 48, 130, 15, config.getRedditPassword());
		usernameField.setFocused(true);
	}
	
	@Override
	protected void drawDialog(int mouseX, int mouseY, float f) {
		super.drawDialog(mouseX, mouseY, f);
		if (!loggingIn) {
			usernameField.setVisible(true);
			passwordField.setVisible(true);
			if (failed) {
				drawCenteredString(fontRendererObj, I18n.format("loginfailpleasetryagain"), dialogX + dialogWidth / 2, dialogY + 4, 0xFF0000);
			}
			drawString(fontRendererObj, I18n.format("username") + ":", dialogX + 5, dialogY + 20,	0xFFFFFF);
			drawString(fontRendererObj, I18n.format("password") + ":", dialogX + 5, dialogY + 50,	0xFFFFFF);
		} else {
			usernameField.setVisible(false);
			passwordField.setVisible(false);
			drawCenteredString(fontRendererObj, I18n.format("loggingin") + "...", dialogX + dialogWidth / 2, dialogY + dialogHeight / 2 - 10, 0xFFFFFF);
		}
		usernameField.drawTextBox();
		passwordField.drawTextBox();
	}
	
	@Override
	public void onLoginSuccess() {
		config.setProperty(VoxelCamConfig.REDDITUSERNAME, usernameField.getText());
		config.setProperty(VoxelCamConfig.REDDITPASSWORD, passwordField.getText());
		Minecraft.getMinecraft().displayGuiScreen(new RedditPostPopup(getParentScreen(), toPost));
	}
	
	@Override
	public void onLoginFailure() {
		loggingIn = false;
		failed = true;
	}
	
	/**
	 * Handle a button event
	 * 
	 * @param guibutton Button or control which sourced the event
	 */
	@Override
 	protected void actionPerformed(GuiButton guibutton) {
		if (guibutton.id == this.btnCancel.id) {
			dialogResult = DialogResult.Cancel;
			closeDialog();
		}
		if (guibutton.id == this.btnOk.id) {
			if (validateDialog()) {
				dialogResult = DialogResult.OK;
				onSubmit();
			}
		}
	}
	
	@Override
	protected void onKeyTyped(char keyChar, int keyCode) {
		super.onKeyTyped(keyChar, keyCode);
		usernameField.textboxKeyTyped(keyChar, keyCode);
		passwordField.textboxKeyTyped(keyChar, keyCode);
	}
	
	@Override
	protected void mouseClickedEx(int mouseX, int mouseY, int button) throws IOException {
		super.mouseClickedEx(mouseX, mouseY, button);
		usernameField.mouseClicked(mouseX, mouseY, button);
		passwordField.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public void onSubmit() {
		loggingIn = true;
		RedditHandler.login(usernameField.getText(), passwordField.getText(), this);
	}

	@Override
	public boolean validateDialog() {
		return true;
	}

}
