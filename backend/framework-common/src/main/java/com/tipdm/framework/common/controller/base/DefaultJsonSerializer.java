package com.tipdm.framework.common.controller.base;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;

/**
 * Created by zhoulong on 2018/8/29.
 */
public class DefaultJsonSerializer extends StdSerializer<String> {

    public DefaultJsonSerializer() {
        this(null);
    }

    public DefaultJsonSerializer(Class<String> t) {
        super(t);
    }

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        // xss策略在此执行
        String safe = HtmlUtils.htmlEscape(value, "utf-8");
        gen.writeString(safe);
    }
}
