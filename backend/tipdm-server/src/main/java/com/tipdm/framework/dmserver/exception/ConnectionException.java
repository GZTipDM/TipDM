package com.tipdm.framework.dmserver.exception;

/**
 * Created by TipDM on 2017/7/17.
 * E-mail:devp@tipdm.com
 */
public class ConnectionException extends RuntimeException{

    public ConnectionException(String message) {
        super(message);
    }

    public ConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
