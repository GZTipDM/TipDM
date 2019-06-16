package com.tipdm.framework.dmserver.redis;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by TipDM on 2017/8/15.
 * E-mail:devp@tipdm.com
 */
public interface MessageDelegate {

    void handleMessage(String message);
    void handleMessage(Map message);
    void handleMessage(byte[] message);
    void handleMessage(Serializable message);
    void handleMessage(Serializable message, String channel);

}
