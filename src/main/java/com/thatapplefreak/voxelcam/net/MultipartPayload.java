package com.thatapplefreak.voxelcam.net;

import org.apache.http.entity.mime.MultipartEntityBuilder;

/**
 * Interface for requests that wish to provide a multi-part payload. A
 * multi-part payload can have multiple string or binary parts. Usually used for
 * transfering images.
 */
public interface MultipartPayload extends Payload<MultipartEntityBuilder> {
}
