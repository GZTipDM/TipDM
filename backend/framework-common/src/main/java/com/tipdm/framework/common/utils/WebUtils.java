package com.tipdm.framework.common.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zhoulong on 2016/12/8.
 * E-mail:zhoulong8513@gmail.com
 */
public class WebUtils extends org.springframework.web.util.WebUtils {

    public final static String AJAX_HEADER = "x-requested-with";
    public final static String XMLHTTPREQUEST = "XMLHttpRequest";

    public static boolean isAjax(HttpServletRequest request) {
        return XMLHTTPREQUEST.equals(request.getHeader(AJAX_HEADER));
    }
}
