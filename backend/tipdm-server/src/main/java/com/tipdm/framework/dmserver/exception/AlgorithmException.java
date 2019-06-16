package com.tipdm.framework.dmserver.exception;

/**
 * Created by TipDM on 2017/1/20.
 * E-mail:devp@tipdm.com
 */
public class AlgorithmException extends RuntimeException {

    public AlgorithmException() {
        super();
    }

    public AlgorithmException(String msg) {
        super(msg);
    }

    public AlgorithmException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public AlgorithmException(Throwable cause) {
        super(cause);
    }

}
