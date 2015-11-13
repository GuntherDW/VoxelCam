package com.thatapplefreak.voxelcam.gui.mainmenu;

import java.io.IOException;

import com.thatapplefreak.voxelcam.gui.manager.GuiScreenShotManager;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;

public class GuiMainMenuWithPhotoButton extends GuiMainMenu {

	private PhotoButton photoBtn;

	@Override
	public void initGui() {
		super.initGui();
		this.photoBtn = new PhotoButton(width / 2 + 104, this.height / 4 + 132);
		buttonList.add(photoBtn);
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton) throws IOException {
		super.actionPerformed(par1GuiButton);
		if (par1GuiButton.equals(photoBtn)) {
			mc.displayGuiScreen(new GuiScreenShotManager());
		}
	}
	
}