package com.tipdm.framework.common;

import com.alibaba.druid.util.JdbcConstants;

import java.util.HashMap;
import java.util.Map;

public final class Constants {

	/**
	 * 日期格式化：yyyyMMdd
	 */
	public final static String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";

	/**
	 * 日期格式化：yyyy-MM-dd
	 */
	public final static String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";

	/**
	 * 日期格式化：yyyy-MM-dd
	 */
	public final static String DATE_FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

	public final static int PAGE_SIZE = 10;
	
//	public final static String CURRENT_USER = "currentUser";
	
	public final static String CURRENT_USER_ID = "currentUserId";

	public final static String CURRENT_USERNAME = "currentUsername";

	public final static String WEBSOCKET_USERNAME = "websocket_username";

	public final static String DEFAULT_PASSWORD = "123456";
	
	public final static int PASSWORD_RETRY_MAX = 10;// 密码重试次数

	public static String RESOURCE_SERVER_NAME = "TipDM-OAuth-server";

	public static String CURRENT_WORKFLOW = "workFlowId";

	public static final String INVALID_CLIENT_DESCRIPTION = "客户端验证失败，如错误的client_id或client_secret";

	public static final int TOKEN_EXPIRES_HOUR = 24;

	public static final long HALF_HOUR = 1 * 60 * 30;

	public static final String CHARACTER = "UTF-8";

	public static final String SPECIAL_CHAR = "-/@#$%^&_+=()";//特殊字符

	//数据表前缀
	public static final String TABLE_PREFIX = "tipdm";

	public final static Map<String, String[]> DRIVERS = new HashMap<String, String[]>();

	static {
//		DRIVERS.put("MySQL", "com.mysql.jdbc.Driver");
//		DRIVERS.put("SQLServer", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
//		DRIVERS.put("Oracle", "oracle.jdbc.driver.OracleDriver");
//		DRIVERS.put("DB2", "com.ibm.db2.jdbc.app.DB2.Driver");
//		DRIVERS.put("HIVE", "org.apache.hive.jdbc.HiveDriver");
//		DRIVERS.put("PostgreSQL", "org.postgresql.Driver");

		DRIVERS.put("jdbc:mysql", new String[]{JdbcConstants.MYSQL_DRIVER, JdbcConstants.MYSQL, "MySQL"});
		DRIVERS.put("jdbc:microsoft", new String[]{JdbcConstants.SQL_SERVER_DRIVER, JdbcConstants.SQL_SERVER, "SQLServer"});
		DRIVERS.put("jdbc:sqlserver", new String[]{JdbcConstants.SQL_SERVER_DRIVER_SQLJDBC4, JdbcConstants.SQL_SERVER, "SQLServer"});
		DRIVERS.put("jdbc:jtds:sqlserver", new String[]{JdbcConstants.SQL_SERVER_DRIVER_JTDS, JdbcConstants.SQL_SERVER, "SQLServer"});
		DRIVERS.put("jdbc:oracle", new String[]{JdbcConstants.ORACLE_DRIVER, JdbcConstants.ORACLE, "Oracle"});
		DRIVERS.put("jdbc:db2", new String[]{"com.ibm.db2.jcc.DB2Driver", JdbcConstants.DB2, "DB2"});
		DRIVERS.put("jdbc:hive2", new String[]{JdbcConstants.HIVE_DRIVER, JdbcConstants.HIVE, "Hive"});
		DRIVERS.put("jdbc:postgresql", new String[]{JdbcConstants.POSTGRESQL_DRIVER, JdbcConstants.POSTGRESQL, "PostgreSQL"});
		//gbase8s
		DRIVERS.put("jdbc:informix-sqli", new String[]{"com.informix.jdbc.IfxDriver", "informix-sqli", "informix-sqli"});
		//gbase8a
		DRIVERS.put("jdbc:gbase", new String[]{"com.gbase.jdbc.Driver", "gbase", "gbase"});
		//dm
		DRIVERS.put("jdbc:dm", new String[]{"dm.jdbc.driver.DmDriver", "dm", "dm"});
		//kingbase
		DRIVERS.put("jdbc:kingbase", new String[]{"com.kingbase.Driver", "kingbase", "kingbase"});
	}
}
