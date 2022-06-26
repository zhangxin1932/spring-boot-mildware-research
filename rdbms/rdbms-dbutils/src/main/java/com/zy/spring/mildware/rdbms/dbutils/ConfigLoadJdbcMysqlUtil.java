package com.zy.spring.mildware.rdbms.dbutils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * 
 * 加载properties配置文件
 * 通过IO读取配置文件,并存储到集合
 * 
 * @author asus
 *
 */
public class ConfigLoadJdbcMysqlUtil {

	private static String driverClass;
	private static String url;
	private static String user;
	private static String password;

	static {
		try {
			// 1.使用Properties处理流
			Properties pro = new Properties();
			InputStream is = ConfigLoadJdbcMysqlUtil.class.getClassLoader().getResourceAsStream("Config_JdbcMysqlUtil.properties");
			// 错误写法:Reader r = new FileReader("Config_JdbcMysqlUtil.properties");
			// 2.使用load()方法加载指定的流
			pro.load(is);
			// 3.使用getProperty(key)，通过key获得需要的值，
			driverClass = pro.getProperty("driverClass");
			url = pro.getProperty("url");
			user = pro.getProperty("user");
			password = pro.getProperty("password");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() {
		try {
			// 1 注册驱动
			Class.forName(driverClass);
			// 2 获得连接
			Connection conn = DriverManager.getConnection(url, user, password);
			return conn;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/*public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
		// 使用类的加载器,获得流
		InputStream is = Config_load_JdbcMysqlUtil.class.getClassLoader().getResourceAsStream("Config_JdbcMysqlUtil.properties");
		// 创建集合,将读取的流放入集合
		Properties pro = new Properties();
		pro.load(is);
		System.out.println(pro);
		// 获取集合中的键值对
		String driverClass = pro.getProperty("driverClass");
		String url = pro.getProperty("url");
		String user = pro.getProperty("user");
		String password = pro.getProperty("password");
		Class.forName(driverClass);
		Connection conn = DriverManager.getConnection(url, user, password);
		System.out.println(conn);
	}*/

}
