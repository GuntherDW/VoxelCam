package com.thatapplefreak.voxelcam;
import static com.thatapplefreak.voxelcam.Translations.SAVING_PLEASE_WAIT;
import static com.thatapplefreak.voxelcam.Translations.SAVING_SCREENSHOT;
import static com.thatapplefreak.voxelcam.Translations.SCREENSHOTS;
import static com.thatapplefreak.voxelcam.Translations.WRITING;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashSet;

import org.lwjgl.input.Keyboard;

import com.mumfrey.liteloader.Configurable;
import com.mumfrey.liteloader.InitCompleteListener;
import com.mumfrey.liteloader.RenderListener;
import com.mumfrey.liteloader.ScreenshotListener;
import com.mumfrey.liteloader.core.LiteLoader;
import com.mumfrey.liteloader.core.LiteLoaderEventBroker.ReturnValue;
import com.mumfrey.liteloader.modconfig.ConfigPanel;
import com.thatapplefreak.voxelcam.gui.mainmenu.FirstRunPopup;
import com.thatapplefreak.voxelcam.gui.mainmenu.MainMenuHandler;
import com.thatapplefreak.voxelcam.gui.manager.GuiScreenShotManager;
import com.thatapplefreak.voxelcam.gui.settings.GuiVoxelCamSettingsPanel;
import com.thatapplefreak.voxelcam.imagehandle.BigScreenshotTaker;
import com.thatapplefreak.voxelcam.imagehandle.ScreenshotIncapable;
import com.thatapplefreak.voxelcam.imagehandle.ScreenshotTaker;
import com.thatapplefreak.voxelcam.net.Poster;
import com.voxelmodpack.common.properties.gui.SettingsPanelManager;
import com.voxelmodpack.common.status.StatusMessage;
import com.voxelmodpack.common.status.StatusMessageManager;
import com.voxelmodpack.common.util.ChatMessageBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

/**
 * Main hook class for VoxelCam
 * 
 * @author thatapplefreak
 * 
 */
public class VoxelCamCore implements ScreenshotListener, InitCompleteListener, RenderListener, Configurable {

	private static VoxelCamCore instance;

	/**
	 * This is the configuration file for the mod
	 */
	private static VoxelCamConfig config = new VoxelCamConfig();

	/**
	 * This is a list of the keys that VoxelCam listens to that are currently in
	 * the down state
	 */
	private HashSet<Integer> heldKeys = new HashSet<Integer>();

	/**
	 * If the mod VoxelMenu is installed this will be true, adds soft dependancy
	 * on VoxelMenu
	 */
	private boolean voxelMenuExists = false;
	private MainMenuHandler mainMenu;
	private StatusMessage savingStatusMessage;
	private ScreenshotTaker screenshot;
	private BigScreenshotTaker bigScreenshot;

	/**
	 * Initialize the mod
	 */
	@Override
	public void init(File configPath) {
		instance = this;
		File screenshotsDir = getScreenshotsDir();
		if (!screenshotsDir.exists()) {
			screenshotsDir.mkdir(); // Make sure that the screenshots directory is there, if not, create it
		}
		mainMenu = new MainMenuHandler();
		screenshot = new ScreenshotTaker(screenshotsDir);

		// Register the Keys that VoxelCam uses
		LiteLoader.getInput().registerKeyBinding(VoxelCamConfig.KEY_OPENSCREENSHOTMANAGER);
		// Register the exposable for tokens.
		LiteLoader.getInstance().registerExposable(Poster.instance, null);

		// Add the configuation panel to VoxelCommons awareness
		SettingsPanelManager.addSettingsPanel("Camera", GuiVoxelCamSettingsPanel.class);
	}

	@Override
	public void upgradeSettings(String version, File configPath, File oldConfigPath) {
	}

	/**
	 * This method is called 20 times per second during the game
	 */
	@Override
	public void onTick(Minecraft minecraft, float partialTicks, boolean inGame, boolean clock) {
		// Ready to take a new screenshot?
		if (bigScreenshot != null) {
			bigScreenshot.onTick(screenshot);
			bigScreenshot = null;
		}
		// Check to see if the user wants to open the screenshot manager
		if (VoxelCamConfig.KEY_OPENSCREENSHOTMANAGER.isPressed() && !Keyboard.isKeyDown(Keyboard.KEY_F3)) {
			if (!heldKeys.contains(VoxelCamConfig.KEY_OPENSCREENSHOTMANAGER.getKeyCode())) {
				if (minecraft.currentScreen instanceof GuiMainMenu || minecraft.currentScreen == null) {
					if (!screenshot.isScreenshotSaving()) {
						minecraft.displayGuiScreen(new GuiScreenShotManager());
					} else {
						ChatMessageBuilder cmb = new ChatMessageBuilder();
						cmb.append("[VoxelCam]", EnumChatFormatting.DARK_RED, false);
						cmb.append(" " + I18n.format(SAVING_PLEASE_WAIT));
						cmb.showChatMessageIngame();
					}
				} else if (minecraft.currentScreen instanceof GuiScreenShotManager) {
					// Dont turn the screenshot manager off if the user is typing into the searchbar
					if (!((GuiScreenShotManager) minecraft.currentScreen).searchBar.isFocused()) {
						minecraft.setIngameFocus();
					}
				}
				heldKeys.add(VoxelCamConfig.KEY_OPENSCREENSHOTMANAGER.getKeyCode());
			}
		} else {
			heldKeys.remove(VoxelCamConfig.KEY_OPENSCREENSHOTMANAGER.getKeyCode());
		}

		// Status Message
		if (minecraft.inGameHasFocus && !minecraft.gameSettings.showDebugInfo) {
			savingStatusMessage.setText(I18n.format(SAVING_SCREENSHOT) + " (" + screenshot.getSavePercent()
					+ "%) " + (screenshot.isWritingToFile() ? I18n.format(WRITING) + "..." : ""));
			savingStatusMessage.setVisible(screenshot.isScreenshotSaving());
		}
	}

	/**
	 * Get the configuration
	 */
	public static VoxelCamConfig getConfig() {
		return config;
	}

	/**
	 * Get the minecraft screenshot directiory
	 */
	public static File getScreenshotsDir() {
		File game = LiteLoader.getGameDirectory();
		if (game == null)
			// game launched without --gameDir?
			game = new File(".").getAbsoluteFile(); // current directory
		return new File(game, "screenshots");
	}

	@Override
	public void onRender() {
		GuiScreen currentScreen = Minecraft.getMinecraft().currentScreen;
		// If this is the users first time running the mod show a welcome screen
		if (currentScreen != null && config.isFirstRun() && !(currentScreen instanceof FirstRunPopup)) {
			Minecraft.getMinecraft().displayGuiScreen(new FirstRunPopup(currentScreen));
		}
	}

	/**
	 * Called immediately before the current GUI is rendered
	 * 
	 * @param currentScreen
	 *            Current screen (if any)
	 */
	@Override
	public void onRenderGui(GuiScreen currentScreen) {
		// If VoxelMenu does not exist modify the Main Menu with the PhotoButton
		if (!voxelMenuExists && currentScreen != null) {
			if (currentScreen instanceof GuiMainMenu) {
				mainMenu.onRenderGui(currentScreen);
			}
		}
	}

	@Override
	public void onRenderWorld() {
	}

	@Override
	public void onSetupCameraTransform() {
	}

	/**
	 * Tell Liteloader the class of the settings panel
	 */
	@Override
	public Class<? extends ConfigPanel> getConfigPanelClass() {
		return GuiVoxelCamSettingsPanel.class;
	}

	@Override
	public boolean onSaveScreenshot(String screenshotName, int width, int height, Framebuffer buffer, ReturnValue<IChatComponent> message) {
		if (!(Minecraft.getMinecraft().currentScreen instanceof ScreenshotIncapable)) {
			if (GuiScreen.isShiftKeyDown()) {
				bigScreenshot = new BigScreenshotTaker(Minecraft.getMinecraft(), config.getPhotoWidth(), config.getPhotoHeight());
			} else {
				screenshot.capture(buffer.framebufferWidth, buffer.framebufferHeight);
			}
		}
		ChatMessageBuilder cmb = new ChatMessageBuilder();
		cmb.append("[VoxelCam]", EnumChatFormatting.DARK_RED, false);
		cmb.append(" " + I18n.format(SAVING_SCREENSHOT));
		message.set(cmb.getMessage());
		return false;
	}

	@Override
	public void onInitCompleted(Minecraft minecraft, LiteLoader loader) {

		// Look for VoxelMenu
		// Why can't there be an interface I can import?
		try {
			Class<?> customMainMenuClass = Class.forName("com.thevoxelbox.voxelmenu.GuiMainMenuVoxelBox");
			Method mRegisterCustomScreen = customMainMenuClass.getDeclaredMethod("registerCustomScreen", String.class, Class.class, String.class);
			mRegisterCustomScreen.invoke(null, "right", GuiScreenShotManager.class, I18n.format(SCREENSHOTS));
			Class<?> ingameGuiClass = Class.forName("com.thevoxelbox.voxelmenu.ingame.GuiIngameMenu");
			mRegisterCustomScreen = ingameGuiClass.getDeclaredMethod("registerCustomScreen", String.class, Class.class, String.class);
			mRegisterCustomScreen.invoke(null, "", GuiScreenShotManager.class, I18n.format(SCREENSHOTS));
			voxelMenuExists = true;
		} catch (ClassNotFoundException ex) {
			// This means VoxelMenu does not exist
			voxelMenuExists = false;
		} catch (Exception e) {
			e.printStackTrace();
		}

		savingStatusMessage = StatusMessageManager.getInstance().getStatusMessage("savingStatus", 1);
		savingStatusMessage.setTitle("VoxelCam");
	}

	public static VoxelCamCore instance() {
		return instance;
	}

	// Leave empty
	@Override
	public String getName() {return null;}
	@Override
	public String getVersion() {return null;}

}
