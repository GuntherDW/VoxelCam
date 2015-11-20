package com.thatapplefreak.voxelcam.net.imgur;

import java.io.File;

import org.apache.commons.lang3.Validate;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import com.thatapplefreak.voxelcam.net.Method;
import com.thatapplefreak.voxelcam.net.MultipartPayload;

public class ImgurUpload extends Imgur<ImgurUploadResponse> implements MultipartPayload {

	private String title;
	private File file;

	public ImgurUpload(File file) {
		this(file.getName(), file);
	}

	public ImgurUpload(String title, File file) {
		super("");
		Validate.notNull(file);
		this.title = title;
		this.file = file;
	}

	@Override
	public void assemblePayload(MultipartEntityBuilder data) {
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
