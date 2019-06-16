package com.tipdm.framework.common.token.impl;

import com.alibaba.fastjson.JSON;
import com.tipdm.framework.common.Constants;
import com.tipdm.framework.common.token.TokenManager;
import com.tipdm.framework.common.token.model.TokenModel;
import com.tipdm.framework.common.utils.PropertiesUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhoulong on 2016/12/6.
 * E-mail:zhoulong8513@gmail.com
 */
@Component("tokenManager")
public class RedisTokenManager implements TokenManager{

    private RedisTemplate<String, Object> redisTemplate;

    private Integer tokenExpiresHour;

    @Resource(name = "redisTemplate")
    public void setRedisTemplate(RedisTemplate redis) {
        this.redisTemplate = redis;
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        String value = PropertiesUtil.getValue("sysconfig/system.properties", "TOKEN_EXPIRES_HOUR");
        if(null != value){
            tokenExpiresHour = Integer.parseInt(value);
        }else {
            tokenExpiresHour = Constants.TOKEN_EXPIRES_HOUR;
        }
    }

    @Override
    public void cachePermissions(TokenModel tokenModel) {
        redisTemplate.boundValueOps(tokenModel.getToken()).set(tokenModel, tokenExpiresHour, TimeUnit.HOURS );
    }

    @Override
    public TokenModel getPermissions(final String token) {

        if(!exists(token)){
            return null;
        }
        Object object = redisTemplate.boundValueOps(token).get();
        TokenModel tokenModel = JSON.parseObject(JSON.toJSONString(object), TokenModel.class);
        return tokenModel;
    }

    @Override
    public boolean exists(final String token) {
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                boolean isExists = redisConnection.exists(token.getBytes());
                if(isExists){
                    redisTemplate.boundValueOps(token).expire(tokenExpiresHour, TimeUnit.HOURS);
                }

                return isExists;
            }
        });
    }

    @Override
    public void deleteToken(String token) {
        redisTemplate.delete(token);
    }
}
