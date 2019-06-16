package com.tipdm.framework.common.controller.base;

import com.tipdm.framework.common.utils.StringKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.HtmlUtils;

import java.beans.PropertyEditorSupport;

/**
 * Created by zhoulong on 2018/8/29.
 */
public class StringEditor extends PropertyEditorSupport {

    private final static Logger LOG = LoggerFactory.getLogger(StringEditor.class);
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if(StringKit.isNotBlank(text)) {
            // xss过滤
            text = HtmlUtils.htmlEscape(text, "utf-8");
        }
        setValue(text);
    }

    @Override
    public String getAsText() {
        Object value = getValue();
        if(value != null) {
            return value.toString();
        }else {
            return "";
        }
    }
}
