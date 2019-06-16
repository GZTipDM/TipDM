package com.tipdm.framework.common.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
 
public class DateKit {
 
    // List of all date formats that we want to parse.
    // Add your own format here.
    private static List<SimpleDateFormat> 
            dateFormats = new ArrayList<SimpleDateFormat>() {
		private static final long serialVersionUID = 1L; 
		{
            add(new SimpleDateFormat("M/dd/yyyy"));
            add(new SimpleDateFormat("dd.M.yyyy"));
            add(new SimpleDateFormat("M/dd/yyyy hh:mm:ss a"));
            add(new SimpleDateFormat("dd.M.yyyy hh:mm:ss a"));
            add(new SimpleDateFormat("dd.MMM.yyyy"));
            add(new SimpleDateFormat("dd-MMM-yyyy"));
            add(new SimpleDateFormat("yyyy-MM-dd"));
            add(new SimpleDateFormat("yyyy/MM/dd"));
            add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            add(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"));
        }
    };
 
    /**
     * Convert String with various formats into java.util.Date
     * 
     * @param input
     *            Date as a string
     * @return java.util.Date object if input string is parsed 
     *          successfully else returns null
     */
    public static Date convertToDate(String input) {
        Date date = null;
        if(null == input) {
            return null;
        }
        for (SimpleDateFormat format : dateFormats) {
            try {
            	format.setLenient(false);
                date = format.parse(input);
            } catch (ParseException e) {
                //尝试其他格式
            }
            if (date != null) {
                break;
            }
        }
 
        return date;
    }

    /**
     * 把给定时间按照给定的格式转为字符串
     * @param pattern
     * @param current
     * @return
     */
    public static String convert2Str(String pattern ,Date current){
        return new SimpleDateFormat(pattern).format(current);
    }
    /**
     * 把给定时间按照给定的格式转为字符串
     * @param pattern
     * @param current
     * @return
     */
    public static String covert2Str(String pattern ,long current){
        return convert2Str(pattern,new Date(current));
    }

}