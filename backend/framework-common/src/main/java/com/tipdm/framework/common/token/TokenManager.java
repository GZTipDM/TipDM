package com.tipdm.framework.common.token;

import com.tipdm.framework.common.token.model.TokenModel;

/**
 * Created by zhoulong on 2016/12/6.
 * E-mail:zhoulong8513@gmail.com
 * 使用Redis缓存用户权限
 */
public interface TokenManager {

    public void cachePermissions(TokenModel tokenModel);

    public TokenModel getPermissions(String token);

    public boolean exists(String token);

    public void deleteToken(String token);
}
