package com.github.taoism.apigateway.sdk.exception;

/**
 * @author Administrator
 */
public class GatewayRuntimeException extends RuntimeException {
    private static final long serialVersionUID = -6881615726580573677L;

    public GatewayRuntimeException() {
        super();
    }

    public GatewayRuntimeException(String message) {
        super(message);
    }

    public GatewayRuntimeException(Throwable cause) {
        super(cause);
    }

    public GatewayRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
