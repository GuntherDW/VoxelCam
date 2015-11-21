package com.thatapplefreak.voxelcam.imagehandle;

import static net.minecraft.client.renderer.GlStateManager.deleteTexture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.client.renderer.texture.TextureUtil;

public abstract class GLImageMemoryHandler {

	/**
	 * Map of all the GL binding integers Key is always the files absolute path
	 */
	private static Map<String, Integer> imageMap = new HashMap<String, Integer>();
	private static HashSet<Integer> loadingImages = new HashSet<Integer>();

	/**
	 * Open GL magic voodoo
	 * 
	 * @param imageFile
	 * @return
	 */
	public static void tryPutTextureIntoMem(final File imageFile, BufferedImage image) {
		int textureId = getImageGLName(imageFile);
		// is the file ready to be loaded?
		if (loadingImages.contains(textureId)) {
			TextureUtil.uploadTextureImageAllocate(textureId, image, true, false);
			// remove the file so we don't load it again
			loadingImages.remove(textureId);
		}
	}

	/**
	 * If the image can be removed from memory free some up
	 * 
	 * @param imageFile
	 */
	public static void requestImageRemovalFromMem(File imageFile) {
		if (imageMap.containsKey(imageFile.getAbsolutePath())) {
			deleteTexture(imageMap.get(imageFile.getAbsolutePath()));
			imageMap.remove(imageFile.getAbsolutePath());
		}
	}

	/**
	 * Flushes All images from memory
	 */
	public static void requestImageFlush() {
		Set<String> keyset = imageMap.keySet();
		for (String s : keyset) {
			deleteTexture(imageMap.get(s));
		}
		imageMap.clear();
	}

	public static int getImageGLName(File f) {
		if (!imageMap.containsKey(f.getAbsolutePath())) {
			int textureId = TextureUtil.glGenTextures();
			imageMap.put(f.getAbsolutePath(), textureId);
			// image is prepared for loading
			loadingImages.add(textureId);
			return textureId;
		}
		return imageMap.get(f.getAbsolutePath());
	}
}
