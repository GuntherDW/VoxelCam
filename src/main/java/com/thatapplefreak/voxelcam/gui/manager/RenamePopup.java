package com.thatapplefreak.voxelcam.gui.manager;

import static com.thatapplefreak.voxelcam.Translations.RENAME;

import java.io.IOException;

import org.apache.commons.io.FilenameUtils;

import com.thatapplefreak.voxelcam.imagehandle.ScreenshotIncapable;
import com.thatapplefreak.voxelcam.io.VoxelCamIO;
import com.voxelmodpack.common.gui.GuiDialogBox;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;

public class RenamePopup extends GuiDialogBox implements ScreenshotIncapable {

	private VoxelCamIO images;
	private GuiTextField renameBox;

	private String oldText;

	public RenamePopup(GuiScreen parentScreen, VoxelCamIO images) {
		super(parentScreen, 200, 75, I18n.format(RENAME));
		this.oldText = FilenameUtils.getBaseName(images.getSelectedPhoto().getName());
	}

	@Override
	protected void onInitDialog() {
		renameBox = new GuiTextField(0xFFFFFF, fontRendererObj, width / 2 - (150 / 2), height / 2 - (16 / 2) - 8, 150, 16);
		renameBox.setText(oldText);
		renameBox.setFocused(true);
	}
	
	@Override
	public void updateScreen() {
		renameBox.updateCursorCounter();
	}

	@Override
	public void onSubmit() {
		images.rename(renameBox.getText());
	}

	@Override
	public boolean validateDialog() {
		return true;
	}

	@Override
	protected void drawDialog(int mouseX, int mouseY, float f) {
		super.drawDialog(mouseX, mouseY, f);
		renameBox.drawTextBox();
	}

	@Override
	protected void mouseClickedEx(int mouseX, int mouseY, int button) throws IOException {
		super.mouseClickedEx(mouseX, mouseY, button);
		renameBox.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	protected void onKeyTyped(char keyChar, int keyCode) {
		renameBox.textboxKeyTyped(keyChar, keyCode);
	}

}