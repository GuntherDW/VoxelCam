package com.thatapplefreak.voxelcam.net.auth;

import static com.thatapplefreak.voxelcam.Translations.PLEASE_ENTER_PIN;

import java.io.IOException;

import com.voxelmodpack.common.gui.GuiDialogBox;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;

public class PinPopup extends GuiDialogBox {

	private GuiTextField pinBox;
	private boolean invalid;
	private AuthFetcher authFetcher;

	public PinPopup(GuiScreen login, AuthFetcher authFetcher) {
		super(login, 200, 75, I18n.format(PLEASE_ENTER_PIN));
		this.authFetcher = authFetcher;
	}

	@Override
	protected void onInitDialog() {
		pinBox = new GuiTextField(0xFFFFFF, fontRendererObj, width / 2 - (150 / 2), height / 2 - (16 / 2) - 8, 150, 16);
	}

	@Override
	public void updateScreen() {
		pinBox.updateCursorCounter();
	}

	@Override
	protected void onDialogClosed() {
		authFetcher.stop();
	}

	@Override
	public void onSubmit() {
		authFetcher.setPin(pinBox.getText());
	}

	@Override
	public boolean validateDialog() {
		return !(invalid = !(pinBox.getText().length() == 7));
	}

	@Override
	protected void drawDialog(int mouseX, int mouseY, float f) {
		super.drawDialog(mouseX, mouseY, f);
		pinBox.drawTextBox();
		if (invalid) {
			drawString(fontRendererObj, "Invalid PIN", dialogX + 10, dialogY + dialogHeight - 16, 0xff0000);
		}
	}

	@Override
	protected void mouseClickedEx(int mouseX, int mouseY, int button) throws IOException {
		super.mouseClickedEx(mouseX, mouseY, button);
		pinBox.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	protected void onKeyTyped(char keyChar, int keyCode) {
		invalid = false;
		pinBox.textboxKeyTyped(keyChar, keyCode);
	}
}
