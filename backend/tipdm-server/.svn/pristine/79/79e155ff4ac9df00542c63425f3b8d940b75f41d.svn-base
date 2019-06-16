package com.tipdm.framework.dmserver.core.algo.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * Created by TipDM on 2018/1/10.
 * E-mail:devp@tipdm.com
 */
public class LogAppender {

    private static final Logger logger = LoggerFactory.getLogger(LogAppender.class);

    public static void info(String mdcTag, String msg){
        MDC.put("component", mdcTag);
        logger.info(msg);
    }

    public static void warn(String mdcTag, String msg){
        MDC.put("component", mdcTag);
        logger.warn(msg);
    }

    public static void error(String mdcTag, String msg){
        MDC.put("component", mdcTag);
        logger.error(msg);
    }
}
