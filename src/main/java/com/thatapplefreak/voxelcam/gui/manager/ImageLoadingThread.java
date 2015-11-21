package com.thatapplefreak.voxelcam.gui.manager;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class ImageLoadingThread extends Thread {

	private static LoadingCache<File, BufferedImage> IMAGES = CacheBuilder.newBuilder()
			.expireAfterAccess(1, TimeUnit.MINUTES) // keep 1 minute
			.maximumSize(10) // max 10 images
			.build(new CacheLoader<File, BufferedImage>() {
				@Override
				public BufferedImage load(File key) throws Exception {
					return load_(key);
				}
			});

	private File imageFile;
	private ScalePhotoFrame callback;

	public ImageLoadingThread(File file, ScalePhotoFrame callback) {
		this.imageFile = file;
		this.callback = callback;
		callback.setImage(IMAGES.getIfPresent(file));
	}

	@Override
	public void run() {
		callback.setImage(IMAGES.getUnchecked(imageFile));
	}

	private static BufferedImage load_(File imageFile) {
		try {
			BufferedImage image = ImageIO.read(imageFile);
			final int MAX_W = 1280;
			final int MAX_H = 720;
			int w = image.getWidth();
			int h = image.getHeight();
			// limit the size to preserve framerate
			if (w > MAX_W || h > MAX_H) {
				float wscale = (float) MAX_W / w;
				float hscale = (float) MAX_H / h;
				float scale = Math.min(wscale, hscale);
				w *= scale;
				h *= scale;
				Image scaled = image.getScaledInstance(w, h, 0);
				image = new BufferedImage(w, h, image.getType());
				Graphics g = image.getGraphics();
				g.drawImage(scaled, 0, 0, null);
				g.dispose();
			}
			return image;

		} catch (IOException e) {
			Throwables.propagate(e);
		}
		return null;
	}

}
