package com.tipdm.framework.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Cron表达式生成工具类
 * @author zhoulong E-mail:zhoulong8513@gmail.com
 * @version 创建时间：2016年9月8日 下午8:28:56
 * 类说明
 */
public class CronExpressionKit {

	private static String formatDateByPattern(Date date,String dateFormat){  
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);  
        String formatTimeStr = null;  
        if (date != null) {  
            formatTimeStr = sdf.format(date);  
        }  
        return formatTimeStr;  
    } 
	
	/*** 
     * convert Date to cron ,eg.  "00 00 21 08 09 ? 2016" 
     * @param date  : 要生成Cron表达式的时间 
     * @return 
     */  
    public static String getCron(java.util.Date  date){  
        String dateFormat="ss mm HH dd MM ? yyyy";  
        return formatDateByPattern(date, dateFormat);  
    }
}
