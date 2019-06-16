package com.tipdm.framework.persist.datasource;

import java.sql.ResultSet;

/**
 * Created by zhoulong on 2017/6/11.
 * E-mail:zhoulong8513@gmail.com
 */
public interface RowToByteArrayConverter {
    byte[] rowToByteArray(ResultSet resultSet);
}
