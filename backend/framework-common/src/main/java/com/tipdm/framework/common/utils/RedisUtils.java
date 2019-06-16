package com.tipdm.framework.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhoulong on 2016/12/14.
 * E-mail:zhoulong8513@gmail.com
 */
public final class RedisUtils {

    private Logger logger = LoggerFactory.getLogger(RedisUtils.class);

    private static RedisTemplate<String, Object> redisTemplate = SpringUtils.getBean("basicTemplate", RedisTemplate.class);


    static {
//        redisTemplate.setValueSerializer();
    }
    /**
     * 批量删除对应的value
     *
     * @param keys
     */
    public static void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * 批量删除key
     *
     * @param pattern
     */
    public static void removePattern(final String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0)
            redisTemplate.delete(keys);
    }

    /**
     * 删除对应的value
     *
     * @param key
     */
    public static void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    /**
     * 判断缓存中是否有对应的value
     *
     * @param key
     * @return
     */
    public static boolean exists(final String key) {

        final byte[] keybytes = redisTemplate.getStringSerializer().serialize(key);
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.exists(keybytes);
            }
        });
    }

    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    public static Object get(final String key) {
        Object result = null;
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        return result;
    }

    public static  <T> T get(final String key, Class<T> t) {
        return redisTemplate.execute(new RedisCallback<T>() {
            @Override
            public T doInRedis(RedisConnection connection)
                    throws DataAccessException {

                if (exists(key)) {
                    byte[] keybytes = redisTemplate.getStringSerializer().serialize(key);
                    byte[] valuebytes = connection.get(keybytes);
                    @SuppressWarnings("unchecked")
                    T value = (T) SerializationKit.deserialize(valuebytes);
                    return value;
                }
                return null;
            }
        });
    }

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean set(final String key, Object value) {

        final byte[] vbytes = SerializationKit.serialize(value);
        try {
            return redisTemplate.execute(new RedisCallback<Boolean>() {
                @Override
                public Boolean doInRedis(RedisConnection connection)
                        throws DataAccessException {
                    connection.set(redisTemplate.getStringSerializer().serialize(key), vbytes);
                    return true;
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @param override 是否覆盖
     * @return
     */
    public static boolean set(final String key, Object value, Boolean override) {

        final byte[] vbytes = SerializationKit.serialize(value);
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection)
                    throws DataAccessException {
                byte[] kbytes = redisTemplate.getStringSerializer().serialize(key);
                if(override){
                    connection.del(kbytes);
                }
                connection.set(kbytes, vbytes);
                return true;
            }
        });
    }

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @param expireTime 过期时间
     * @return
     */
    public static boolean set(final String key, Object value, final Long expireTime) {
        final byte[] vbytes = SerializationKit.serialize(value);
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection)
                    throws DataAccessException {
                byte[] keyByte = redisTemplate.getStringSerializer().serialize(key);
                connection.set(keyByte, vbytes);
                return connection.expire(keyByte, expireTime);
            }
        });
    }

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @param expireTime 过期时间
     * @param override 是否覆盖
     * @return
     */
    public static boolean set(final String key, Object value, final Long expireTime, Boolean override) {
        final byte[] vbytes = SerializationKit.serialize(value);
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection)
                    throws DataAccessException {
                byte[] kbytes = redisTemplate.getStringSerializer().serialize(key);
                if(override){
                    connection.del(kbytes);
                }
                connection.set(kbytes, vbytes);
                return connection.expire(kbytes, expireTime);
            }
        });
    }


    /**
     * 追加
     * @param key
     * @param value
     * @return
     */
    public static boolean append(final String key, final String value) {

        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection)
                    throws DataAccessException {
                byte[] keyByte = redisTemplate.getStringSerializer().serialize(key);
                byte[] valuebytes = connection.get(keyByte);
                String tmp = ((String)SerializationKit.deserialize(valuebytes));

                if(StringKit.isNotBlank(tmp)){
                    tmp += "\n" + value;
                } else {
                    tmp = value;
                }

                final byte[] vbytes = SerializationKit.serialize(tmp);
                connection.set(redisTemplate.getStringSerializer().serialize(key), vbytes);
                return true;
            }
        });
    }

    /**
     * 追加，同时设定过期时间
     * @param key
     * @param value
     * @param expireTime
     * @return
     */
    public static boolean append(final String key, final String value, final Long expireTime) {
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection)
                    throws DataAccessException {
                byte[] keyByte = redisTemplate.getStringSerializer().serialize(key);
                byte[] valuebytes = connection.get(keyByte);
                String tmp = ((String)SerializationKit.deserialize(valuebytes));

                if(StringKit.isNotBlank(tmp)){
                    tmp += "\n" + value;
                } else {
                    tmp = value;
                }

                final byte[] vbytes = SerializationKit.serialize(tmp);
                connection.set(redisTemplate.getStringSerializer().serialize(key), vbytes);
                return connection.expire(keyByte, expireTime);
            }
        });
    }

    /**
     * 缓存Map
     * @param key
     * @param map
     * @return
     */
    public static void setMap(String key,Map<String,Object> map)
    {

        HashOperations hashOperations = redisTemplate.opsForHash();
        if(null != map) {
            hashOperations.putAll(key, map);
        }

    }

    /**
     * 获得缓存的Map
     * @param key
     * @return
     */
    public static Map<String, Object> getMap(String key)
    {
        BoundHashOperations<String, String, Object> boundHashOperations = redisTemplate.boundHashOps(key);
        return boundHashOperations.entries();
    }

    /**
     * 从redis缓存的Map中获取指定key
     * @param key
     * @param field
     * @return
     */
    public static Object getFromMap(final String key, final String field){

        return redisTemplate.opsForHash().get(key, field);
    }

    public static void removeFromMap(final String key, final String field){
        redisTemplate.opsForHash().delete(key, field);
    }

    /**
     * 向key对应的map中添加缓存对象
     * @param key   cache对象key
     * @param field map对应的key
     * @param obj   field对应的值
     */
    public static void putToMap(String key, String field, Object obj){
        redisTemplate.opsForHash().put(key, field, obj);
    }

    /**
     * 向key对应的map中添加缓存对象
     * @param key   cache对象key
     * @param field map对应的key
     * @param obj   field对应的值
     * @param expire 过期时间，单位：hour
     */
    public static void putToMap(String key, String field, Object obj, Long expire){
        redisTemplate.opsForHash().put(key, field, obj);
        redisTemplate.expire(key, expire, TimeUnit.HOURS);
    }

}
