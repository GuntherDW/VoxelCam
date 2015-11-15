package com.thatapplefreak.voxelcam.upload;

import java.io.File;

public interface IUploader<Response> {

	void upload(File file, UploadCallback<Response> callback);

}
