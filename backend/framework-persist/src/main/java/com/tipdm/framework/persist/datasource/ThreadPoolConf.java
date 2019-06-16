package com.tipdm.framework.persist.datasource;

import com.tipdm.framework.common.utils.StringKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by zhoulong on 2017/1/9.
 * E-mail:zhoulong8513@gmail.com
 */
public class ThreadPoolConf {

    private final static Logger logger = LoggerFactory.getLogger(ThreadPoolConf.class);

    private int maxThreadCount = 20; // 允许的最大线程数

    private int threadRecordNum = 5000;//单个线程处理的记录数

    private int count = 0;

    public ThreadPoolConf(int count){
        this.count = count;
        initPoolSize();
    }

    public int getPoolSize() {
        return maxThreadCount;
    }

    public int getRecordNumber() {
        return threadRecordNum;
    }

    private void initPoolSize(){

        try {
            Properties prop = new Properties();

            InputStream is = ThreadPoolConf.class.getClassLoader().getResourceAsStream("/sysconfig/system.properties");
            prop.load(is);

            String max = prop.getProperty("maxThreadCount");
            if(StringKit.isNotBlank(max)){
                maxThreadCount = Integer.parseInt(max);
            }
            String num = prop.getProperty("threadRecordNum");
            if(StringKit.isNotBlank(num)){
                threadRecordNum = Integer.parseInt(num);
            }
        } catch (Exception e) {
            logger.error("thread pool conf", e);
        }

        if(count > (maxThreadCount * threadRecordNum)){
            threadRecordNum = count / maxThreadCount;
        } else {
            maxThreadCount = (count % threadRecordNum == 0) ? count/threadRecordNum : (count/threadRecordNum)+1;
        }
    }

}
