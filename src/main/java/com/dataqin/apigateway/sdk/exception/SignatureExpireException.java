package com.dataqin.apigateway.sdk.exception;

/**
 * @author Administrator
 */
public class SignatureExpireException extends GatewayRuntimeException {
    private static final long serialVersionUID = 9117025907204504082L;


    public SignatureExpireException() {
        super();
    }

    public SignatureExpireException(String message) {
        super(message);
    }

    public SignatureExpireException(Throwable cause) {
        super(cause);
    }

    public SignatureExpireException(String message, Throwable cause) {
        super(message, cause);
    }

}
