package com.dataqin.apigateway.sdk.exception;

/**
 * @author Administrator
 */
public class SignatureWrongNumberException extends GatewayRuntimeException {
    private static final long serialVersionUID = 7193706006156464007L;

    public SignatureWrongNumberException() {
        super();
    }

    public SignatureWrongNumberException(String message) {
        super(message);
    }

    public SignatureWrongNumberException(Throwable cause) {
        super(cause);
    }

    public SignatureWrongNumberException(String message, Throwable cause) {
        super(message, cause);
    }
}
