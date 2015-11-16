package com.thatapplefreak.voxelcam.net.twitter;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import com.google.common.io.Files;
import com.thatapplefreak.voxelcam.net.Callback;
import com.thatapplefreak.voxelcam.net.FileSizeException;
import com.thatapplefreak.voxelcam.net.MultipartPayload;
import com.thatapplefreak.voxelcam.net.PayloadException;
import com.thatapplefreak.voxelcam.net.Request;

public class TwitterImage extends TwitterAuth implements Request<TwitterImageResponse>, MultipartPayload {

	private static final String REQUEST_URL = "https://upload.twitter.com/1.1/media/upload.json";

	private static final long MAX_SIZE = (long) Math.pow(1024, 2);

	private File file;
	private Callback<TwitterImageResponse> callback;

	public TwitterImage(File file, Callback<TwitterImageResponse> callback) throws FileSizeException {
		if (file.length() > MAX_SIZE) {
			throw new FileSizeException(formatException(file.length(), MAX_SIZE));
		}
		this.file = file;
		this.callback = callback;
	}

	private static String formatException(long size, long max) {
		String ssize = FileUtils.byteCountToDisplaySize(size);
		String smax = FileUtils.byteCountToDisplaySize(max);
		return String.format("File is %s but max is %s.", ssize, smax);
	}

	@Override
	public String getRequestUrl() {
		return REQUEST_URL;
	}

	@Override
	public void assemblePayload(MultipartEntityBuilder builder) throws PayloadException {
		try {
			builder.addBinaryBody("media", Files.asByteSource(file).openStream());
		} catch (IOException e) {
			throw new PayloadException(e);
		}
	}

	@Override
	public Class<TwitterImageResponse> getResponseClass() {
		return TwitterImageResponse.class;
	}

	@Override
	public void onResponse(TwitterImageResponse response) {
		// TODO Auto-generated method stub
		if (callback != null)
			callback.onCompleted(response);
	}

}
