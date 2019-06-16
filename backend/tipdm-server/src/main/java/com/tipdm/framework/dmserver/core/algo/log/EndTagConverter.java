package com.tipdm.framework.dmserver.core.algo.log;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * Created by TipDM on 2017/3/2.
 * E-mail:devp@tipdm.com
 */
public class EndTagConverter extends ClassicConverter {

    @Override
    public String convert(ILoggingEvent iLoggingEvent) {
        String color = "</div>";
        return color;
    }
}
