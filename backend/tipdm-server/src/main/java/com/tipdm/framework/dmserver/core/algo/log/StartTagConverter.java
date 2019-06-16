package com.tipdm.framework.dmserver.core.algo.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * Created by TipDM on 2017/3/2.
 * E-mail:devp@tipdm.com
 */
public class StartTagConverter extends ClassicConverter {

    @Override
    public String convert(ILoggingEvent iLoggingEvent) {
        String color = "";
        Level level = iLoggingEvent.getLevel();
        switch (level.levelInt){
            case 40000:
//                color = "<div style=\"color:#ff2424; text-align:left;\">";
                color = "<div style=\"color:#ff2424;\">";
                break;
            case 30000:
//                color = "<div style=\"color:#fcc166; text-align:left;\">";
                color = "<div style=\"color:#fcc166;\">";
                break;
            default:
//                color = "<div style=\"color:#0ccd02; text-align:left;\">";
                color = "<div style=\"text-align:left;\">";
                break;
        }
        return color;
    }
}
