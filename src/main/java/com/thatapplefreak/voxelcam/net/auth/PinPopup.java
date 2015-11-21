package com.thatapplefreak.voxelcam.net.auth;

import static com.thatapplefreak.voxelcam.Translations.PLEASE_ENTER_PIN;

import java.io.IOException;

import com.voxelmodpack.common.gui.GuiDialogBox;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;

public class PinPopup extends GuiDialogBox {

	private GuiTextField pinBox;
	private boolean ready;
	private boolean cancelled;

	public PinPopup(LoginPopup login) {
		super(login.getParentScreen(), 200, 75, I18n.format(PLEASE_ENTER_PIN));
	}

	@Override
	protected void onInitDialog() {
		pinBox = new GuiTextField(0xFFFFFF, fontRendererObj, width / 2 - (150 / 2), height / 2 - (16 / 2) - 8, 150, 16);
		// BrowserOpener.openURLstringInBrowser(TwitterHandler.requestToken.getAuthorizationURL());
	}

	@Override
	protected void onDialogClosed() {
		cancelled = true;
	}

	@Override
	public void onSubmit() {
		ready = true;
	}

	@Override
	public boolean validateDialog() {
		return pinBox.getText().length() == 7;
	}

	@Override
	protected void drawDialog(int mouseX, int mouseY, float f) {
		super.drawDialog(mouseX, mouseY, f);
		pinBox.drawTextBox();
	}

	@Override
	protected void mouseClickedEx(int mouseX, int mouseY, int button) throws IOException {
		super.mouseClickedEx(mouseX, mouseY, button);
		pinBox.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	protected void onKeyTyped(char keyChar, int keyCode) {
		pinBox.textboxKeyTyped(keyChar, keyCode);
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (guibutton.id == this.btnCancel.id) {
			this.dialogResult = DialogResult.Cancel;
			this.closeDialog();
		}
		if (guibutton.id == this.btnOk.id) {
			if (this.validateDialog()) {
				this.dialogResult = DialogResult.OK;
				this.onSubmit();
			}
		}
	}

	public void goToPostGUI() {
		mc.displayGuiScreen(getParentScreen());
	}

	String getPin() {
		if (ready)
			return pinBox.getText();
		if (cancelled)
			throw new UserCancelledException();
		return null;
	}
}
