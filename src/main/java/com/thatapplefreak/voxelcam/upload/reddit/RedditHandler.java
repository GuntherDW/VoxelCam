package com.thatapplefreak.voxelcam.upload.reddit;

import java.io.File;
import java.net.URI;

import org.apache.http.client.utils.URIBuilder;

import com.google.common.base.Throwables;
import com.thatapplefreak.voxelcam.net.Callback;
import com.thatapplefreak.voxelcam.net.imgur.ImgurUploadResponse;
import com.thatapplefreak.voxelcam.upload.imgur.ImgurHandler;

public class RedditHandler {

	private static final String SUBMIT_URL = "http://www.reddit.com/submit";

	/**
	 * Post image to reddit by opening the browser to the submit page.
	 *
	 * @param postTitle
	 * @param screenshot
	 */
	public static void doRedditPost(final File screenshot, final Callback<URI> callback) {
		ImgurHandler.doImgur(screenshot, new Callback<ImgurUploadResponse>() {
			@Override
			public void onSuccess(ImgurUploadResponse response) {

				if (response.isSuccessful()) {
					try {
						// just send to the submit page
						URI submit = new URIBuilder(SUBMIT_URL)
								.addParameter("url", response.getData().getLink())
								.build();
						callback.onSuccess(submit);
					} catch (Exception e) {
						Throwables.propagate(e);
					}
				} else {
					// propagate
					Throwables.propagate(new Exception(response.getData().getError()));
				}
			}

			@Override
			public void onFailure(Throwable t) {
				callback.onFailure(t);
			}
		});
	}
}
