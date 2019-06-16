package com.tipdm.framework.persist.datasource;

import com.tipdm.framework.persist.dialect.DmPostgreSQL94Dialect;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.dialect.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
/**
 * Created by zhoulong on 2017/11/11.
 * E-mail:zhoulong8513@gmail.com
 */
public class Hibernates {
    public Hibernates() {
    }

    public static void initLazyProperty(Object proxyedPropertyValue) {
        Hibernate.initialize(proxyedPropertyValue);
    }

    public static String getDialect(DynamicDataSource dynamicDataSource) {
        DataSource dataSource = dynamicDataSource.getDataSource();
        return getDialect(dataSource);
    }

    public static String getDialect(DataSource dataSource) {
        String jdbcUrl = getJdbcUrlFromDataSource(dataSource);
        if(StringUtils.contains(jdbcUrl, ":h2:")) {
            return H2Dialect.class.getName();
        } else if(StringUtils.contains(jdbcUrl, ":mysql:")) {
            return MySQL5InnoDBDialect.class.getName();
        } else if(StringUtils.contains(jdbcUrl, ":oracle:")) {
            return Oracle10gDialect.class.getName();
        } else if(StringUtils.contains(jdbcUrl, ":postgresql:")) {
            return DmPostgreSQL94Dialect.class.getName();
        } else if(StringUtils.contains(jdbcUrl, ":sqlserver:")) {
            return SQLServer2008Dialect.class.getName();
        } else {
            throw new IllegalArgumentException("Unknown Database of " + jdbcUrl);
        }
    }

    private static String getJdbcUrlFromDataSource(DataSource dataSource) {
        Connection connection = null;

        String e;
        try {
            connection = dataSource.getConnection();
            if(connection == null) {
                throw new IllegalStateException("Connection returned by DataSource [" + dataSource + "] was null");
            }

            e = connection.getMetaData().getURL();
        } catch (SQLException var11) {
            throw new RuntimeException("Could not get database url", var11);
        } finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException var10) {
                    ;
                }
            }

        }

        return e;
    }
}
