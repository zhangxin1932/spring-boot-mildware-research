package com.zy.spring.mildware.rdbms.dbutils;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 
 * DBUtils是java编程中的数据库操作实用工具，小巧简单实用。
 * DBUtils封装了对JDBC的操作，简化了JDBC操作，可以少写代码。
 * Dbutils三个核心功能介绍:
 * QueryRunner中提供对sql语句操作的API.
 * ResultSetHandler接口，用于定义select操作后，怎样封装结果集.
 * DbUtils类，它就是一个工具类,定义了关闭资源与事务处理的方法
 * 
 * QueryRunner核心类
 * update(Connection conn, String sql, Object... params) ，用来完成表数据的增加、删除、更新操作
 * query(Connection conn, String sql, ResultSetHandler<T> rsh, Object... params) ，用来完成表数据的查询操作
 * 
 * 
 * 
 * @author asus
 *
 */
public class DbUtilUpdate {
	private static Connection conn = ConfigLoadJdbcMysqlUtil.getConnection();
	
	public static void main(String[] args) {
		// 1.采用DbUtils封装插入语句的方法
		// insert();
		// 2.采用DbUtils封装修改语句的方法
		// update();
		// 3.采用DbUtils封装删除语句的方法
		delete();
	}
	
	// 3.采用DbUtils封装删除语句的方法
	public static void delete() {
		// 3.1创建QueryRunner类的对象
		QueryRunner qr = new QueryRunner();
		// 3.2传参
		String sql = "delete from sort where sid=?";
		try {
			int row = qr.update(conn, sql, 3);
			System.out.println(row);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 3.3关流
		DbUtils.closeQuietly(conn);
		
	}
	
	// 2.采用DbUtils封装修改语句的方法
	public static void update() {
		// 2.1创建QueryRunner类的对象
		QueryRunner qr = new QueryRunner();
		// 2.2传参
		String sql = "update sort set sname=?,sprice=?,sdesc=? where sid=?";
		Object[] obj = {"鱼儿",30.21,"青海鱼",3};
		try {
			int i = qr.update(conn, sql, obj);
			System.out.println(i);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 2.3关流
		DbUtils.closeQuietly(conn);
		
		
	}
	
	
	
	// 1.采用DbUtils封装插入语句的方法
	public static void insert() {
		// 1.创建QueryRunner类的对象
		QueryRunner qr = new QueryRunner();
		// 2.分别传入其参数
		String sql = "insert into sort(sname,sprice,sdesc) values(?,?,?)";
		Object[] obj = {"洗发水",20.18,"飘柔"};
		// update(Connection conn, String sql, Object... params) ，用来完成表数据的增加、删除、更新操作
		try {
			int i = qr.update(conn, sql, obj);
			System.out.println(i);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 3.关流
		DbUtils.closeQuietly(conn);
		
	}
	
	
	
}
