package com.tipdm.framework.dmserver.shiro.filter;

import com.alibaba.fastjson.JSON;
import com.tipdm.framework.dmserver.shiro.token.BearerAuthenticationToken;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by TipDM on 2016/12/8.
 * E-mail:devp@tipdm.com
 */
@SuppressWarnings("all")
public class BearerTokenAuthenticatingFilter extends AccessControlFilter {

    private final static Logger logger = LoggerFactory.getLogger(BearerTokenAuthenticatingFilter.class);

    private String[] allowOrigin;

    private String allowMethods;

    private String allowHeaders;

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        cors((HttpServletRequest) servletRequest,  (HttpServletResponse)servletResponse);
        if(((HttpServletRequest) servletRequest).getMethod().equals("OPTIONS")) {
            ((HttpServletResponse) servletResponse).setStatus(HttpServletResponse.SC_OK);
            return true;
        }
        //从请求URL中获取token
        String token = ((HttpServletRequest)servletRequest).getHeader("accessToken");
        BearerAuthenticationToken bearerAuthenticationToken = new BearerAuthenticationToken(token);
        try {
            getSubject(servletRequest, servletResponse).login(bearerAuthenticationToken);
            return true;
        }catch(Exception ex){
            logger.error(ex.getMessage());
            Map<String, Object> result = new HashMap<>();
            result.put("message", "accessToken已失效，请重新获取！");
            result.put("status", 401);
            onAccessFail(servletRequest, servletResponse, JSON.toJSONString(result));
            return false;
        }
    }

    //登录失败时默认返回401状态码
    private void onAccessFail(ServletRequest servletRequest, ServletResponse servletResponse, String errorMessage) throws IOException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.write(errorMessage);
        out.flush();
        out.close();
    }


    private void cors(HttpServletRequest request, HttpServletResponse response){
        if(null != this.allowOrigin) {
            String origin = request.getHeader("Origin");
            if(ArrayUtils.contains(this.allowOrigin, origin)) {
                response.setHeader("Access-Control-Allow-Origin", origin);
            }
        }

        if(StringUtils.isNotBlank(this.allowMethods)) {
            response.setHeader("Access-Control-Allow-Methods", this.allowMethods);
        }

        response.setHeader("Access-Control-Max-Age", "3600");

        if(StringUtils.isNotBlank(this.allowHeaders)) {
            response.setHeader("Access-Control-Allow-Headers", this.allowHeaders);
        }
    }

    public void setAllowOrigin(String[] allowOrigin) {
        this.allowOrigin = allowOrigin;
    }

    public void setAllowMethods(String allowMethods) {
        this.allowMethods = allowMethods;
    }

    public void setAllowHeaders(String allowHeaders) {
        this.allowHeaders = allowHeaders;
    }
}
