package com.tipdm.framework.dmserver.utils;

import com.alibaba.druid.sql.PagerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

/**
 * Created by TipDM on 2017/10/26.
 * E-mail:devp@tipdm.com
 */
public class PagerKit {

    private final static Logger logger = LoggerFactory.getLogger(PagerKit.class);
    public static String limit(String sql, String dbType, int offset, int count) {

        String limitSQL;
        if(sql.toLowerCase().contains(" union ")){
            sql = "SELECT * FROM (" + sql + ") as TEMP";
        }
        switch (dbType){
            case "informix-sqli":

//                break;
            case "dm":

//                break;
            case "gbase":

//                break;
//                limitSQL = MessageFormatter.arrayFormat("SELECT * FROM ( {} ) TEMP limit {} offset {}", new Object[]{sql, count, offset}).getMessage();
                limitSQL = MessageFormatter.arrayFormat("SELECT * FROM ( {} )  as TEMP limit {} ", new Object[]{sql, count}).getMessage();
                break;
            case "hive":
                limitSQL = "SELECT * FROM (" + sql + ") as TEMP limit 100";
                logger.info("Hive SQL: {}", limitSQL);
                break;
            case "kingbase":
                limitSQL = MessageFormatter.arrayFormat("SELECT TOP {}, {} * from ( {} ) as TEMP", new Object[]{offset, count, sql}).getMessage();
                break;
            default:
                limitSQL = PagerUtils.limit(sql, dbType, offset, count);
                break;
        }
        logger.info("DATABASE TYPE: {}, final SQL: {}", dbType, limitSQL);
        return limitSQL;
    }
}
