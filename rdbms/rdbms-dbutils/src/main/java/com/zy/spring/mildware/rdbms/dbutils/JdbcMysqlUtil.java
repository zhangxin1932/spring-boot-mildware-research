package com.zy.spring.mildware.rdbms.dbutils;

import java.sql.*;

public class JdbcMysqlUtil {

	// 不允许new
	private JdbcMysqlUtil() {};
	
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	// 封装获取conn的方法
	public static Connection getConnection(String ip,int port,String dbName,String user,String pwd) throws SQLException {
		String url = "jdbc:mysql://" + ip + ":" + port + "/" + dbName;
		Connection conn = DriverManager.getConnection(url,user,pwd);
		return conn;
	}
	// 封装关流的方法:适用于查询
	public static void close(Connection conn,Statement st,ResultSet rs) {
		if(rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(st != null) {
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	// 封装关流的方法:适用于增删改
	public static void close(Connection conn,Statement st) {
		if(st != null) {
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
}
