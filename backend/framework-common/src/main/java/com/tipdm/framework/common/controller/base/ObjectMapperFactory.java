package com.tipdm.framework.common.controller.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * Created by zhoulong on 2018/8/29.
 */
public class ObjectMapperFactory {

    public static ObjectMapper mapper = Jackson2ObjectMapperBuilder.json().build();

    static {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(String.class, new DefaultJsonDeserializer());
        module.addSerializer(String.class, new DefaultJsonSerializer());
        mapper.registerModule(module);
    }

    public static ObjectMapper getMapper() {
        return mapper;
    }
}
