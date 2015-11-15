package com.thatapplefreak.voxelcam.upload;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.voxelmodpack.common.net.upload.IUploadCompleteCallback;
import com.voxelmodpack.common.net.upload.ThreadMultipartPostUpload;

public abstract class Upload<Response> implements IUploadCompleteCallback {
	/**
	 * Method for request
	 * 
	 * @author Mumfrey
	 */
	protected enum Method {
		GET, POST, PUT, DELETE
	}

	/**
	 * Gson deserialiser
	 */
	private static Gson gson = new Gson();

	/**
	 * HTTP method
	 */
	private final Method method;
	/**
	 * URL to get/post to
	 */
	private final String url;
	/**
	 * Class to wrap JSON responses in
	 */
	private final Class<Response> responseClass;

	/**
	 * Upload thread
	 */
	private ThreadMultipartPostUpload uploadThread;
	/**
	 * HTTP POST data
	 */
	private Map<String, Object> postData = new HashMap<String, Object>();
	/**
	 * Parsed JSON response
	 */
	private Response response;
	/**
	 * Object to call back
	 */
	private UploadCallback<Response> callback;

	/**
	 * True if the process completed
	 */
	private boolean isComplete;
	/**
	 * True if this process was started
	 */
	public boolean started = false;

	public Upload(Method method, String url, Class<Response> responseClass) {
		this.url = url;
		this.method = method;
		this.responseClass = responseClass;
	}

	@Override
	public void onUploadComplete(String responseData) {
		this.isComplete = true;

		if (this.uploadThread.httpResponseCode / 100 != 2) {
			if (this.callback != null) {
				this.callback.onHTTPFailure(this.uploadThread.httpResponseCode, this.uploadThread.httpResponse);
			}

			return;
		}

		this.response = gson.fromJson(responseData, this.responseClass);

		if (this.callback != null) {
			this.callback.onCompleted(this.response);
		}
	}

	public boolean isComplete() {
		return this.isComplete;
	}

	public Response getResponse() {
		return this.response;
	}

	protected abstract void assemble(Map<String, Object> data);

	/**
	 * Gets the authorization for the upload. May return null if there is none.
	 * 
	 * @return The authorization portion of the header
	 */
	protected abstract String getAuthorization();

	public void start(UploadCallback<Response> callback) {
		if (this.started) {
			throw new IllegalStateException("Task already started");
		}

		this.started = true;
		this.callback = callback;

		this.assemble(postData);

		String authorization = getAuthorization();
		this.uploadThread = new ThreadMultipartPostUpload(this.method.toString(), this.url, this.postData,
				authorization, this);
		this.uploadThread.start();
	}
}
