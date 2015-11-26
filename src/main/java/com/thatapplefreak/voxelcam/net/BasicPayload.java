package com.thatapplefreak.voxelcam.net;

import org.apache.http.client.methods.RequestBuilder;

/**
 * Interface for requests that just want a basic payload. A basic payload is
 * key=value pairs separated by &.
 * <p>
 * Example: <code>kyle=jew&cartman=fat</code>
 */
public interface BasicPayload extends Payload<RequestBuilder> {
}
