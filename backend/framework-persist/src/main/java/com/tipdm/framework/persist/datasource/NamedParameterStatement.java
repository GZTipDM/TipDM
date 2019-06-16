package com.tipdm.framework.persist.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhoulong on 2017/10/27.
 * E-mail:zhoulong8513@gmail.com
 */
public class NamedParameterStatement {

    private final static Logger logger = LoggerFactory.getLogger(NamedParameterStatement.class);

    private PreparedStatement prepStmt;
    private List<String> fields = new ArrayList<String>();

    public NamedParameterStatement(Connection conn, String sql) throws SQLException {
        int pos = 0;
        while((pos = sql.indexOf(":", pos)) != -1) {
            int end = sql.substring(pos).indexOf(" ");
            if (end == -1)
                end = sql.length();
            else
                end += pos;
            String prefix = sql.substring(0, pos);
            if(prefix.trim().endsWith("=")) {
                sql = sql.substring(0, pos) + "?" + sql.substring(end);
            } else {
                pos = end;
            }
        }
        logger.info("PreparedStatement SQL: {}", sql);
        try {
            prepStmt = conn.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        }catch (Exception ex) {
            //如果不支持游标
            prepStmt = conn.prepareStatement(sql);
        }
    }

    public PreparedStatement getPreparedStatement() {
        return prepStmt;
    }
    public ResultSet executeQuery() throws SQLException {
        return prepStmt.executeQuery();
    }
    public void close() throws SQLException {
        prepStmt.close();
    }

    public void setInt(String name, int value) throws SQLException {
        prepStmt.setInt(getIndex(name), value);
    }

    public void setDate(String name, java.sql.Date value) throws SQLException {
        prepStmt.setDate(getIndex(name), value);
    }
    public void setObject(String name, Object value) throws SQLException {
        prepStmt.setObject(getIndex(name), value);
    }

    private int getIndex(String name) {
        return fields.indexOf(name)+1;
    }

    public void setFetchSize(int fetchSize) throws SQLException {
        prepStmt.setFetchSize(fetchSize);
    }

    public boolean hasNamedParameter(String name){
        return fields.contains(name);
    }

    public void setParameter(Map<String, Object> parameters) throws SQLException {
        for(Map.Entry<String, Object> entry : parameters.entrySet()){
            if(hasNamedParameter(entry.getKey())){
                setObject(entry.getKey(), entry.getValue());
            }
        }
    }

    static
    public void main(String[] args){

        String sql = "select * from sg_db_20170925_new:temp_result where name = :name and age=:age";
        int pos = 0;
        while((pos = sql.indexOf(":", pos)) != -1) {
            int end = sql.substring(pos).indexOf(" ");
            if (end == -1)
                end = sql.length();
            else
                end += pos;
            String prefix = sql.substring(0, pos);
            if(prefix.trim().endsWith("=")) {
                sql = sql.substring(0, pos) + "?" + sql.substring(end);
            } else {
                pos = end;
            }
        }

        System.out.println(sql);
    }
}
