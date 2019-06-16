package com.tipdm.framework.persist.jdbc;

import org.slf4j.helpers.MessageFormatter;

import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by zhoulong on 2017/11/20.
 * E-mail:zhoulong8513@gmail.com
 */
public class DynamicLoadDriver {

    /**
     *
     * @param url
     * @param driverClass
     * @throws Exception
     */
    static
    public void loadDriver(URL url, String driverClass) throws SQLException {

        try {
            URLClassLoader classLoader = new URLClassLoader(new URL[] {url});
            Driver driver = (Driver) Class.forName(driverClass, true, classLoader).newInstance();
            DriverManager.registerDriver(new DriverShim(driver));
        } catch (Exception ex){
            throw new SQLException(MessageFormatter.arrayFormat("无法注册驱动：{}， 驱动文件：{}", new Object[]{driverClass, url.getPath()}).getMessage());
        }
    }
}
