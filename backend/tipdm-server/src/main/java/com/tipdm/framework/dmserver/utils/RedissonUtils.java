package com.tipdm.framework.dmserver.utils;

import com.tipdm.framework.common.utils.PropertiesUtil;
import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.codec.SerializationCodec;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;

import java.util.Map;

//import org.redisson.Config;
//import org.redisson.Redisson;
//import org.redisson.RedissonClient;
//import org.redisson.SingleServerConfig;

/**
 * Created by TipDM on 2018/1/19.
 * E-mail:devp@tipdm.com
 */
public final class RedissonUtils {

    private static RedissonClient redissonClient;

    private RedissonUtils(){

    }
    //初始化
    static {
        try {
            Map<String, String> properties = PropertiesUtil.getProperties("sysconfig/redis.properties");
            String redisServer = "redis://" + properties.get("redis.host") + ":" + properties.get("redis.port");
            String redisPassword = properties.get("redis.password");
            int maxTotal = Integer.valueOf(properties.getOrDefault("redis.maxTotal", "100"));
            int minIdle = Integer.valueOf(properties.getOrDefault("redis.minIdle", "5"));
            Config cfg = new Config();
            SingleServerConfig singleServerConfig = cfg.useSingleServer().setAddress(redisServer);
            singleServerConfig.setPassword(redisPassword);
            singleServerConfig.setConnectionPoolSize(maxTotal);
            singleServerConfig.setConnectionMinimumIdleSize(minIdle);
            cfg.setCodec(new SerializationCodec());
            redissonClient = Redisson.create(cfg);
            properties = null;
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static RLiveObjectService getRLOService(){
        return redissonClient.getLiveObjectService();
    }

    public static RKeys getKeys(){
        return redissonClient.getKeys();
    }

    public static long delete(String... key){
        RKeys rKeys = getKeys();
        return rKeys.delete(key);
    }

    public static long deleteByPattern(String pattern){
        RKeys rKeys = getKeys();
        return rKeys.deleteByPattern(pattern);
    }

    /**
     * 获取字符串对象
     *
     * @param objectName
     * @return
     */
    public static <T> RBucket<T> getRBucket(String objectName) {
        RBucket<T> bucket = redissonClient.getBucket(objectName);
        return bucket;
    }

    /**
     * 获取Map对象
     *
     * @param objectName
     * @return
     */
    public static <K, V> RMap<K, V> getRMap(String objectName) {
        RMap<K, V> map = redissonClient.getMap(objectName);
        return map;
    }

    /**
     * 获取RMapCache对象
     *
     * @param objectName
     * @return
     */
    public static <K, V> RMapCache<K, V> getRMapCache(String objectName) {
        RMapCache<K, V> map = redissonClient.getMapCache(objectName);
        return map;
    }

    /**
     * 获取有序集合
     *
     * @param objectName
     * @return
     */
    public static <V> RSortedSet<V> getRSortedSet(String objectName) {
        RSortedSet<V> sortedSet = redissonClient.getSortedSet(objectName);
        return sortedSet;
    }

    /**
     * 获取集合
     *
     * @param objectName
     * @return
     */
    public static <V> RSet<V> getRSet(String objectName) {
        RSet<V> rSet = redissonClient.getSet(objectName);
        return rSet;
    }

    /**
     * 获取列表
     *
     * @param objectName
     * @return
     */
    public static <V> RList<V> getRList(String objectName) {
        RList<V> rList = redissonClient.getList(objectName);
        return rList;
    }

    /**
     * 获取队列
     *
     * @param objectName
     * @return
     */
    public static <V> RQueue<V> getRQueue(String objectName) {
        RQueue<V> rQueue = redissonClient.getQueue(objectName);
        return rQueue;
    }

    /**
     * 获取双端队列
     *
     * @param objectName
     * @return
     */
    public static <V> RDeque<V> getRDeque(String objectName) {
        RDeque<V> rDeque = redissonClient.getDeque(objectName);
        return rDeque;
    }

    /**
     * @param objectName
     * @return
     */
    public <V> RBlockingQueue<V> getRBlockingQueue(String objectName) {
        RBlockingQueue rb = redissonClient.getBlockingQueue(objectName);
        return rb;
    }

    /**
     * 获取锁
     *
     * @param objectName
     * @return
     */
    public static RLock getRLock(String objectName) {
        RLock rLock = redissonClient.getLock(objectName);
        return rLock;
    }

    /**
     * 获取原子数
     *
     * @param objectName
     * @return
     */
    public static RAtomicLong getRAtomicLong(String objectName) {
        RAtomicLong rAtomicLong = redissonClient.getAtomicLong(objectName);
        return rAtomicLong;
    }

    /**
     * 获取闭锁
     *
     * @param objectName
     * @return
     */
    public static RCountDownLatch getRCountDownLatch(String objectName) {
        RCountDownLatch rCountDownLatch = redissonClient.getCountDownLatch(objectName);
        return rCountDownLatch;
    }

    /**
     * 获取消息的Topic
     *
     * @param objectName
     * @return
     */
    public static <M> RTopic<M> getRTopic(String objectName) {
        RTopic<M> rTopic = redissonClient.getTopic(objectName);
        return rTopic;
    }
}
