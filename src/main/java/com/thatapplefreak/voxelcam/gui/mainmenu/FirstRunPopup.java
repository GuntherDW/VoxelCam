package com.thatapplefreak.voxelcam.gui.mainmenu;

import com.mumfrey.liteloader.core.LiteLoader;
import static com.thatapplefreak.voxelcam.Translations.*;
import com.thatapplefreak.voxelcam.VoxelCamConfig;
import com.thatapplefreak.voxelcam.VoxelCamCore;
import com.voxelmodpack.common.gui.GuiDialogBox;
import com.voxelmodpack.common.util.BrowserOpener;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class FirstRunPopup extends GuiDialogBox {

	private static final ResourceLocation avatarPNG = new ResourceLocation("voxelcam", "textures/avatar.png");
	
	private GuiButton forumLink;

	public FirstRunPopup(GuiScreen parentScreen) {
		super(parentScreen, 320, 150, "VoxelCam");
	}

	@Override
	protected void onInitDialog() {
		btnCancel.visible = false;
		btnOk.displayString = I18n.format(OK);
		forumLink = new GuiButton(-111195, btnCancel.xPosition, btnCancel.yPosition, 60, 20, I18n.format("moreinfo"));
		buttonList.add(forumLink);
	}

	@Override
	protected void drawDialog(int mouseX, int mouseY, float f) {
		try {
			drawString(fontRendererObj, I18n.format(WELCOME_LINE_1) + " " + LiteLoader.getInstance().getMod("VoxelCam").getVersion() + " ", dialogX + 5, dialogY + 5, 0xFFFFFF);
		} catch (Exception e) {
		}
		drawString(fontRendererObj, I18n.format(KEYBINDINGS) + ":", dialogX + 5, dialogY + 20, 0x990000);
		drawString(fontRendererObj, "H - " + I18n.format(WELCOME_LINE_2), dialogX + 10, dialogY + 30, 0x990000);
		drawString(fontRendererObj, "Shift + F2 - " + I18n.format(WELCOME_LINE_3), dialogX + 10, dialogY + 40, 0x990000);
		drawString(fontRendererObj, "F7 - " + I18n.format(WELCOME_LINE_4), dialogX + 10, dialogY + 50, 0x990000);

		drawString(fontRendererObj, I18n.format(DEVELOPER) + ":", dialogX + 5, dialogY + 70, 0x00FFFF);
		drawTexturedModalRect(avatarPNG, dialogX + 10, dialogY + 80, dialogX + 75, dialogY + 140, 0, 0, 259, 256);
		drawString(fontRendererObj, "thatapplefreak", dialogX + 6, dialogY + 141, 0xFFFF00);

		drawString(fontRendererObj, "Twitter: @xApplefreak", dialogX + 80, dialogY + 80, 0x4099FF);
		drawString(fontRendererObj, "Reddit: thatapplefreak", dialogX + 80, dialogY + 90, 0xff4500);
		drawString(fontRendererObj, "MinecraftForum: thatapplefreak", dialogX + 80, dialogY + 100, 0x80ba59);
	}
	
	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (guibutton.equals(forumLink)) {
			BrowserOpener.openURLstringInBrowser("http://bit.ly/16LXtjV");
		}
		super.actionPerformed(guibutton);
	}

	@Override
	public void onSubmit() {
		VoxelCamCore.getConfig().setProperty(VoxelCamConfig.FIRSTRUN, false);
	}

	@Override
	public boolean validateDialog() {
		return true;
	}

}
