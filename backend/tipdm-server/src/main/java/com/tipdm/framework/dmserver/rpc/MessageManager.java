package com.tipdm.framework.dmserver.rpc;

import com.tipdm.framework.service.dmserver.MessageService;
import com.weibo.api.motan.config.ProtocolConfig;
import com.weibo.api.motan.config.RefererConfig;
import com.weibo.api.motan.config.RegistryConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by TipDM on 2019/5/6.
 */
public class MessageManager {

    private static Map<String, MessageService> clientMap = new HashMap<>();

    public static MessageService getService(String rpcAddress){
        MessageService service = clientMap.get(rpcAddress);
        if(service != null){
            return service;
        }
        RefererConfig<MessageService> serviceReferer = new RefererConfig<MessageService>();

        // 设置接口及实现类
        serviceReferer.setInterface(MessageService.class);
        serviceReferer.setRequestTimeout(1000);

        // 配置注册中心直连调用
        RegistryConfig registry = new RegistryConfig();

        //use direct registry
        registry.setRegProtocol("direct");
        registry.setAddress(rpcAddress);

        serviceReferer.setRegistry(registry);
        // 配置RPC协议
        ProtocolConfig protocol = new ProtocolConfig();
        protocol.setId("motan");
        protocol.setName("motan");
        serviceReferer.setProtocol(protocol);

        service = serviceReferer.getRef();
        clientMap.put(rpcAddress, service);
        return service;
    }
}
