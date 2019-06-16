package com.tipdm.framework.common.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * @author zhoulong E-mail:zhoulong8513@gmail.com
 * @version 创建时间：2016年12月4日 下午1:36:49 
 * 字符串工具类 继承自org.apache.commons.lang3.StringUtils
 */
@SuppressWarnings("all")
public class StringKit extends StringUtils {

	/**
	 * 使用base64将UUID加密成16位长度的字符串
	 * @return
	 */
	public static String getBase64FromUUID() {
		
		Base64 base64 = new Base64();
		UUID uuid = UUID.randomUUID();
		ByteBuffer buffer = ByteBuffer.wrap(new byte[16]);
		buffer.putLong(uuid.getMostSignificantBits());
		buffer.putLong(uuid.getLeastSignificantBits());
		
		return base64.encodeBase64URLSafeString(buffer.array());
	}

	/**
	 * 生成UUID,包含连接线
	 * @return uuid
     */
	public static String uuid(){
		return uuid(false);
	}

	/**
	 *
	 * @param removeConnectionLine 是否去除连接线
	 * @return
     */
	public static String uuid(boolean removeConnectionLine){
		UUID uuid = UUID.randomUUID();
		if(removeConnectionLine){
			return uuid.toString().replaceAll("\\-", "");
		}
		return uuid.toString();
	}


	/**
	 * 将object转换为可逆的base64字符串
	 * @param object
	 * @return
     */
	static
	public String toBase64String(Object object){

		if(null == object){
			throw new NullPointerException("object can not be null");
		}
		Base64 base64 = new Base64();
		String json = JSON.toJSONString(object);
		return base64.encodeBase64URLSafeString(json.getBytes());
	}

	/**
	 *
	 * @param string 要转换的base64字符串
	 * @param clazz 目标类
	 * @param <T>
     * @return T
     */
	static
	public <T> T parseObjectFromBase64String(String string, Class<T> clazz){
		Base64 base64 = new Base64();
		byte[] bytes = base64.decode(string);
		String json = new String(bytes);
		try {
			return JSON.parseObject(json, clazz);
		}catch (Exception ex){
			return null;
		}
	}
    /**
     * 打印字符串数组
     * @param args
     * @return
     */
    public static String arr2String(String[] args){
        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        for(String str : args){
            buffer.append(str).append(",");
        }
        return buffer.substring(0,buffer.length() -1 ) + "]";
    }

    /**
     * 打印字符串数组
     * @param args
     * @param splitter
     * @return
     */
    public static String arr2String(String[] args,String splitter){
        StringBuffer buffer = new StringBuffer();
        for(String str : args){
            buffer.append(str).append(splitter);
        }
        return buffer.substring(0,buffer.length() -2 ) ;
    }
}
