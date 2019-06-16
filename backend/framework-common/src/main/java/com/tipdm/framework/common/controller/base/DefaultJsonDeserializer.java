package com.tipdm.framework.common.controller.base;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;

/**
 * Created by zhoulong on 2018/8/29.
 */
public class DefaultJsonDeserializer extends StdDeserializer<String> {

    public DefaultJsonDeserializer() {
        this(null);
    }

    public DefaultJsonDeserializer(Class<String> t) {
        super(t);
    }

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException {
        String value = p.getValueAsString();
        if (StringUtils.isEmpty(value)) {
            return value;
        } else {
            value = HtmlUtils.htmlEscape(value.toString(), "utf-8");
            return value;
        }
    }
}
