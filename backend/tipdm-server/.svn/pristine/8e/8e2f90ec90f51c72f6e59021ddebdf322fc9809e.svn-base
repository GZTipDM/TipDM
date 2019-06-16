package com.tipdm.framework.dmserver.websocket;

import com.alibaba.fastjson.JSON;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;

import com.tipdm.framework.common.utils.StringKit;
import com.tipdm.framework.dmserver.utils.RedissonUtils;
import com.tipdm.framework.dmserver.websocket.dto.Message;
import org.redisson.api.RMapCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.tipdm.framework.dmserver.utils.Constants;

/**
 * Created by TipDM on 2018/1/13.
 * E-mail:devp@tipdm.com
 */
@SuppressWarnings("all")
public class SocketServer {

    private static final Logger LOG = LoggerFactory.getLogger(SocketServer.class);

    private static SocketIOServer server;

    private String rpcAddress;

    private int port = 9020;

    private static RMapCache<String, Map<String, String>> clientMap;

    public SocketServer() {
    }

    public void init() throws Exception {
        Configuration config = new Configuration();
        config.setPort(this.port);
        config.setMaxFramePayloadLength(1048576);
        config.setMaxHttpContentLength(1048576);
        server = new SocketIOServer(config);
        clientMap = RedissonUtils.getRMapCache(Constants.WS_CLIENTS);

        server.addConnectListener(new ConnectListener() {
            public void onConnect(SocketIOClient client) {
                String accessToken = client.getHandshakeData().getSingleUrlParam("accessToken");
                String sessionId = client.getSessionId().toString();
                Map<String, String> mapping = new HashMap<>();
                mapping.put("rpcAddress", rpcAddress);
                mapping.put("sessionId", sessionId);
                //记录session<->rpc服务的映射
                clientMap.put(accessToken, mapping, 7, TimeUnit.DAYS);
            }
        });
        server.start();
    }

    public void stop() {
        if (null != server) {
            server.stop();
        }

    }

    public void setRpcAddress(String rpcAddress) {
        this.rpcAddress = rpcAddress;
    }

    public void setPort(int port) {
        this.port = port;
    }


    /**
     * 推送流程执行状态
     *
     * @param workFlowId
     * @param message
     */
    public static void notifyWorkFlowExecStatus(String workFlowId, String sessionId, Message message) {
        LOG.info("客户端ID: {}", sessionId);
        SocketIOClient client = server.getClient(UUID.fromString(sessionId));
        if(null != client) {
            String content = JSON.toJSONString(message);
            LOG.info("ID: {}, 节点状态: {}", workFlowId, content);
            client.sendEvent(workFlowId, content);
            LOG.info("推送成功...");
        }
    }

    /**
     * 发送数据源同步结果
     * @param accessToken
     * @param content
     */
    public static void sendDataSyncResult(String accessToken, String content){
        LOG.info("accessToken: {}", accessToken);
        Map<String, String> mapping = clientMap.get(accessToken);
        String sessionId = mapping.get("sessionId");
        SocketIOClient client = server.getClient(UUID.fromString(sessionId));
        if (client != null) {
            LOG.info("数据同步结果: {}", content);
            client.sendEvent("dataSyncResult", content);
        }
    }
}
