package com.thatapplefreak.voxelcam.net.imgur;

import java.io.File;

import org.apache.commons.lang3.Validate;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import com.thatapplefreak.voxelcam.net.Callback;
import com.thatapplefreak.voxelcam.net.Method;
import com.thatapplefreak.voxelcam.net.MultipartPayload;
import com.thatapplefreak.voxelcam.net.PayloadException;

public class ImgurUpload extends Imgur<ImgurUploadResponse> implements MultipartPayload {

	private String title;
	private File file;

	public ImgurUpload(File file, Callback<ImgurUploadResponse> callback) {
		this(file.getName(), file, callback);
	}

	public ImgurUpload(String title, File file, Callback<ImgurUploadResponse> callback) {
		super("", callback);
		Validate.notNull(file);
		this.title = title;
		this.file = file;
	}

	@Override
	public void assemblePayload(MultipartEntityBuilder data) throws PayloadException {
		data.addBinaryBody("image", file);
		data.addTextBody("type", "image/png");
		data.addTextBody("title", title);
	}

	@Override
	public Method getMethod() {
		return Method.POST;
	}

	@Override
	public Class<ImgurUploadResponse> getResponseClass() {
		return ImgurUploadResponse.class;
	}
}
