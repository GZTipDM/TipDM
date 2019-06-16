package com.tipdm.framework.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.Thread.UncaughtExceptionHandler;
/**
 * Created by zhoulong on 2016/12/29.
 * E-mail:zhoulong8513@gmail.com
 */
public class UncheckedExceptionhandler implements UncaughtExceptionHandler {

    private final static Logger logger = LoggerFactory.getLogger(UncheckedExceptionhandler.class);

    private UncaughtExceptionHandler defaultUEH;

    public UncheckedExceptionhandler(){
        this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
    }
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        logger.error( "uncaughtException", e);
//        defaultUEH.uncaughtException(t, e);
        throw new RuntimeException(e);
    }
}
