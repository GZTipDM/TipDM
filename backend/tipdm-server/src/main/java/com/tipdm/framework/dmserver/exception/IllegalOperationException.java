package com.tipdm.framework.dmserver.exception;

/**
 * Created by TipDM on 2017/8/13.
 * E-mail:devp@tipdm.com
 */
public class IllegalOperationException extends RuntimeException {

    public IllegalOperationException(String message) {
        super(message);
    }

    public IllegalOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
