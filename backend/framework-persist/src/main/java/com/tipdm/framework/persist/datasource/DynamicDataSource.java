package com.tipdm.framework.persist.datasource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tipdm.framework.common.utils.RedisUtils;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhoulong on 2017/11/11.
 * E-mail:zhoulong8513@gmail.com
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    private final static Logger logger = LoggerFactory.getLogger(DynamicDataSource.class);

    private String finalDataSourceName = "FinalDataSource";

    // 默认数据源，也就是主库
    protected DataSource masterDataSource;

//    private DataSourceConfig dataSourceConfig;

    private PoolProperties poolProperties;

    // 保存动态创建的数据源
    private static final Map targetDataSource = new HashMap<>();

    @Override
    protected DataSource determineTargetDataSource() {
        // 根据数据库选择方案，拿到要访问的数据库
        String dataSourceName = determineCurrentLookupKey();
        if("defaultDataSource".equals(dataSourceName)) {
            // 访问默认主库
            return masterDataSource;
        }

        // 根据数据库名字，从已创建的数据库中获取要访问的数据库
        DataSource dataSource = (DataSource) targetDataSource.get(dataSourceName);
        if(null == dataSource) {
            // 从Redis中获取要访问的数据库，如果没有则创建一个
            dataSource = this.selectDataSource(dataSourceName);
        }
        return dataSource;
    }

    @Override
    protected String determineCurrentLookupKey() {
        if(!RedisUtils.exists(finalDataSourceName)){
            return "defaultDataSource";
        }
        return finalDataSourceName;
    }

    public void addTargetDataSource(String key, DataSource dataSource) {
        this.targetDataSource.put(key, dataSource);
    }


    /**
     * 该方法为同步方法，防止并发创建两个相同的数据库
     * 使用双检锁的方式，防止并发
     * @param dbType
     * @return
     */
    private synchronized DataSource selectDataSource(String dbType) {
        // 再次从数据库中获取，双检锁
        DataSource obj = (DataSource)this.targetDataSource.get(dbType);
        if (null != obj) {
            return obj;
        }
        // 为空则创建数据库
        DataSource dataSource = this.getDataSource(dbType);
        if (null != dataSource) {
            // 将新创建的数据库保存到map中
            this.setDataSource(dbType, dataSource);
            return dataSource;
        }else {
            throw new DataSourceLookupFailureException("创建数据源失败！");
        }
    }

    /**
     * 查询对应数据库的信息
     * @param dbtype
     * @return
     */
    private DataSource getDataSource(String dbtype) {

        if("defaultDataSource".equals(dbtype)){
            return masterDataSource;
        }
        // 查询所需信息
        JSONObject dataBase = JSON.parseObject(RedisUtils.get(finalDataSourceName, String.class));
        String host = dataBase.getString("host");
        int port = dataBase.getInteger("port");
        String dbName = dataBase.getString("name");
        String url = MessageFormatter.arrayFormat("jdbc:postgresql://{}:{}/{}", new Object[]{host, port, dbName}).getMessage();
        String username = dataBase.getString("username");
        String password = dataBase.getString("password");
        DataSource dataSource = createDataSource("org.postgresql.Driver",url, username, password);
        return dataSource;
    }


    //创建数据源
    private DataSource createDataSource(String driverClassName,String url,
                                             String username, String password) {

        poolProperties.setUrl(url);
        poolProperties.setUsername(username);
        poolProperties.setPassword(password);
        poolProperties.setDriverClassName(driverClassName);
        DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource(poolProperties);
        return dataSource;
    }

    private void setDataSource(String type, DataSource dataSource) {
        this.addTargetDataSource(type, dataSource);
    }

/*    @Override
    public void setTargetDataSources(Map targetDataSources) {
        // TODO Auto-generated method stub
        super.setTargetDataSources(targetDataSources);
        // 重点：通知container容器数据源发生了变化
        afterPropertiesSet();
    }*/


    /**
     * 该方法重写为空，因为AbstractRoutingDataSource类中会通过此方法将，targetDataSources变量中保存的数据源交给resolvedDefaultDataSource变量
     * 在本方案中动态创建的数据源保存在了本类的targetDataSource变量中。如果不重写该方法为空，会因为targetDataSources变量为空报错
     * 如果仍然想要使用AbstractRoutingDataSource类中的变量保存数据源，则需要在每次数据源变更时，调用此方法来为resolvedDefaultDataSource变量更新
     */
    @Override
    public void afterPropertiesSet() {
    }

    public DataSource getMasterDataSource() {
        return masterDataSource;
    }

    public void setMasterDataSource(DataSource masterDataSource) {
        this.masterDataSource = masterDataSource;
    }

    public DataSource getDataSource() {
        return determineTargetDataSource();
    }

    public void setPoolProperties(PoolProperties poolProperties) {
        this.poolProperties = poolProperties;
    }

    public void setFinalDataSourceName(String finalDataSourceName) {
        this.finalDataSourceName = finalDataSourceName;
    }
}
