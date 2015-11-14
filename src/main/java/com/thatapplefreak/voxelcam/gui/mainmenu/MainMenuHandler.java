package com.thatapplefreak.voxelcam.gui.mainmenu;

import java.util.List;

import com.mumfrey.liteloader.core.runtime.Obf;
import com.mumfrey.liteloader.util.PrivateFields;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class MainMenuHandler {

	private static final PrivateFields<GuiScreen, List<GuiButton>> BUTTON_LIST = new PrivateFields<GuiScreen, List<GuiButton>>(
			GuiScreen.class, new Obf("field_146292_n", "n", "buttonList") {
			}) {
	};

	private GuiScreen screen;
	private List<GuiButton> buttonList;

	public void onRenderGui(GuiScreen screen) {
		if (this.screen != screen) {
			// there's a new screen. Update things.
			this.screen = screen;
			buttonList = BUTTON_LIST.get(screen);
		}
		// buttons get reset when screen resolution changes, check for it.
		if (!listContainsType(buttonList, PhotoButton.class))
			insertButton(screen);
	}

	private void insertButton(GuiScreen gui) {
		PhotoButton button = new PhotoButton(gui.width / 2 + 104, gui.height / 4 + 132);
		buttonList.add(button);
	}

	private <T> boolean listContainsType(List<?> list, Class<T> type) {
		for (Object o : list) {
			if (type.isInstance(o)) {
				return true;
			}
		}
		return false;
	}

}