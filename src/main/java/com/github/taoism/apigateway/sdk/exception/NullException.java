package com.github.taoism.apigateway.sdk.exception;

public class NullException extends GatewayRuntimeException {
    private static final long serialVersionUID = -7204556289593045601L;

    public NullException() {
        super();
    }

    public NullException(String message) {
        super(message);
    }

    public NullException(Throwable cause) {
        super(cause);
    }

    public NullException(String message, Throwable cause) {
        super(message, cause);
    }
}
