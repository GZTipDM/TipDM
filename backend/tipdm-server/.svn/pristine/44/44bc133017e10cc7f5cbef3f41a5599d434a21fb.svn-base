package com.tipdm.framework.dmserver.shiro.token;

import com.tipdm.framework.common.token.TokenManager;
import com.tipdm.framework.common.utils.SpringUtils;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * Created by TipDM on 2016/12/6.
 * E-mail:devp@tipdm.com
 */
public class BearerAuthenticationToken implements AuthenticationToken {

    private String token;

    private TokenManager tokenManager = SpringUtils.getApplicationContext().getBean("tokenManager", TokenManager.class);

    public BearerAuthenticationToken(){

    }

    public BearerAuthenticationToken(String token){
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
