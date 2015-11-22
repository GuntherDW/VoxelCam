package com.thatapplefreak.voxelcam.upload.twitter;

import static com.thatapplefreak.voxelcam.Translations.COMPOSE_TWEET;
import static com.thatapplefreak.voxelcam.Translations.POST;
import static com.thatapplefreak.voxelcam.Translations.POST_TO;
import static com.thatapplefreak.voxelcam.Translations.REMAINING_LETTERS;
import static com.thatapplefreak.voxelcam.Translations.UPLOADING;

import java.io.File;
import java.io.IOException;

import com.thatapplefreak.voxelcam.gui.upload.UploadFailedPopup;
import com.thatapplefreak.voxelcam.gui.upload.UploadSuccessPopup;
import com.thatapplefreak.voxelcam.net.Callback;
import com.thatapplefreak.voxelcam.net.Request;
import com.thatapplefreak.voxelcam.net.twitter.TwitterDestroy;
import com.thatapplefreak.voxelcam.net.twitter.TwitterStatusResponse;
import com.voxelmodpack.common.gui.GuiDialogBox;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;

public class TwitterPostPopup extends GuiDialogBox implements Callback<TwitterStatusResponse> {

	private boolean uploading = false;

	private GuiTextField textbox;
	private File toPost;
	private int tweetLengh = 100;

	public TwitterPostPopup(GuiScreen parentScreen, File toPost) {
		super(parentScreen, 210, 90, I18n.format(POST_TO, "Twitter"));
		this.toPost = toPost;
	}

	@Override
	protected void onInitDialog() {
		btnOk.displayString = I18n.format(POST);
		textbox = new GuiTextField(0xFFFFFF, fontRendererObj, width / 2 - (200 / 2), height / 2 - (16 / 2) - 8, 200, 16);
		textbox.setMaxStringLength(tweetLengh);
		textbox.setFocused(true);
	}

	@Override
	protected void drawDialog(int mouseX, int mouseY, float f) {
		super.drawDialog(mouseX, mouseY, f);

		if (uploading) {
			buttonList.clear();
			drawCenteredString(fontRendererObj, I18n.format(UPLOADING) + "...", width / 2, height / 2, 0xFFFFFF);
		} else {
			textbox.drawTextBox();
			drawString(fontRendererObj, I18n.format(COMPOSE_TWEET) + ":", dialogX + 5, height / 2 - 28, 0xFFFFFF);
			// align right
			String remaining = I18n.format(REMAINING_LETTERS, tweetLengh - textbox.getText().length()) + " ";
			int txtw = fontRendererObj.getStringWidth(remaining);
			drawString(fontRendererObj, remaining, dialogX + dialogWidth -txtw, height / 2 + 5, 0xFFFFFF);
		}
	}

	@Override
	protected void mouseClickedEx(int mouseX, int mouseY, int button) throws IOException {
		super.mouseClickedEx(mouseX, mouseY, button);
		textbox.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	protected void onKeyTyped(char keyChar, int keyCode) {
		textbox.textboxKeyTyped(keyChar, keyCode);
	}

	@Override
	public void updateScreen() {
		textbox.updateCursorCounter();
	}

	@Override
	public void onSubmit() {
	}

	@Override
	public boolean validateDialog() {
		TwitterHandler.doTwitter(textbox.getText(), toPost, this);
		uploading = true;
		return false;
	}

	@Override
	public void onSuccess(TwitterStatusResponse response) {
		mc.displayGuiScreen(new UploadSuccessPopup(getParentScreen(), response.getUrl(), (Request<?>) new TwitterDestroy(response.getId())));
	}

	@Override
	public void onFailure(Throwable t) {
		mc.displayGuiScreen(new UploadFailedPopup(getParentScreen(), t.getMessage()));
	}

}
