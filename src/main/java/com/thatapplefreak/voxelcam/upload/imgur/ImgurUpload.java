package com.thatapplefreak.voxelcam.upload.imgur;

import java.io.File;
import java.util.Map;

/**
 * Imgur agent to upload a file, callers should get the URL from the response
 * data
 * 
 * @author Mumfrey
 */
public class ImgurUpload extends Imgur<ImgurUploadResponse> {

	private static final String ROUTE = "image";

	private final File file;

	private String title;

	private String description;

	public ImgurUpload(File imageFile) {
		super(Method.POST, ImgurUpload.ROUTE, ImgurUploadResponse.class);
		this.file = imageFile;
	}

	public ImgurUpload withTitle(String title) {
		this.title = title;
		return this;
	}

	public ImgurUpload withDescription(String description) {
		this.description = description;
		return this;
	}

	@Override
	protected void assemble(Map<String, Object> data) {
		data.put("image", this.file);
		data.put("type", "file");

		if (this.title != null)
			data.put("title", this.title);
		if (this.description != null)
			data.put("description", this.description);
	}
}
