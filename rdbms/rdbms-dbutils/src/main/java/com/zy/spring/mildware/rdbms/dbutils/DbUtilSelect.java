package com.zy.spring.mildware.rdbms.dbutils;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 
 * 1.QueryRunner实现查询操作
 * query(Connection conn, String sql, ResultSetHandler<T> rsh, Object... params) ，用来完成表数据的查询操作
 * 
 * 2.ResultSetHandler结果集处理类
 * ArrayHandler	将结果集中的第一条记录封装到一个Object[]数组中，数组中的每一个元素就是这条记录中的每一个字段的值
 * ArrayListHandler	将结果集中的每一条记录都封装到一个Object[]数组中，将这些数组在封装到List集合中。
 * BeanHandler	将结果集中第一条记录封装到一个指定的javaBean中。
 * BeanListHandler	将结果集中每一条记录封装到指定的javaBean中，将这些javaBean在封装到List集合中
 * ColumnListHandler	将结果集中指定的列的字段值，封装到一个List集合中
 * ScalarHandler	它是用于单数据。例如select count(*) from 表操作。
 * MapHandler	将结果集第一行封装到Map集合中,Key 列名, Value 该列数据
 * MapListHandler	将结果集第一行封装到Map集合中,Key 列名, Value 该列数据,Map集合存储到List集合
 * 
 * 3.JavaBean
 * JavaBean就是一个类，在开发中常用封装数据。具有如下特性
 * 需要实现接口：java.io.Serializable ，通常实现接口这步骤省略了，不会影响程序。
 * 提供私有字段：private 类型 字段名;
 * 提供getter/setter方法：
 * 提供无参构造
 * 
 * 
 * 
 * @author asus
 *
 */
public class DbUtilSelect {
	private static Connection conn = ConfigLoadJdbcMysqlUtil.getConnection();

	public static void main(String[] args) {
		// 1.采用ArrayHandler处理结果集,将结果的首行存储到Object[]数组中
		// arrHandler();
		// 2.采用ArrayListHandler处理结果集,将结果的每行存储到Object[]数组中,然后存到list集合中
		// arrListHandler();
		// 3.采用BeanHandler处理结果集,将结果集中第一条记录封装到一个指定的javaBean中
		// beanHandler();
		// 4.采用BeanListHandler处理结果集,将结果集中每一条记录封装到指定的javaBean中，将这些javaBean在封装到List集合中
		// beanListHandler();
		// 5.采用ColumnListHandler处理结果集,将结果集中指定的列的字段值，封装到一个List集合中
		// columnListHandler();
		// 6.采用ScalarHandler处理结果集,它是用于单数据。例如select count(*) from 表操作。
		// scalarHandler();
		// 7.MapHandler	将结果集第一行封装到Map集合中,Key 列名, Value 该列数据
		// mapHandler();
		// 8.MapListHandler	将结果集第一行封装到Map集合中,Key 列名, Value 该列数据,Map集合存储到List集合
		mapListHandler();
	}
	
	// 8.MapListHandler	将结果集第一行封装到Map集合中,Key 列名, Value 该列数据,Map集合存储到List集合
	public static void mapListHandler() {
		// 8.1创建QueryRunner对象
		QueryRunner qr = new QueryRunner();
		// 8.2传参
		String sql = "select * from sort";
		List<Map<String, Object>> list = null;
		try {
			list = qr.query(conn, sql, new MapListHandler());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		for(Map<String,Object> map : list) {
			for(String key : map.keySet()) {
				System.out.print(key + "\t" + map.get(key) + "\t");
			}
			System.out.println();
		}
		
	}
	
	// 7.MapHandler	将结果集第一行封装到Map集合中,Key 列名, Value 该列数据
	public static void mapHandler() {
		// 7.1创建QueryRunner对象
		QueryRunner qr = new QueryRunner();
		// 7.2传参
		String sql = "select * from sort";
		Map<String, Object> map = null;
		try {
			map = qr.query(conn, sql, new MapHandler());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		for(String key : map.keySet()) {
			System.out.println(key + "\t" + map.get(key));
		}
		// 7.3关流
		DbUtils.closeQuietly(conn);
		
	}
	
	
	// 6.采用ScalarHandler处理结果集,它是用于单数据。例如select count(*) from 表操作。
	public static void scalarHandler() {
		// 6.1创建QueryRunner对象
		QueryRunner qr = new QueryRunner();
		// 6.2传参
		String sql = "select count(*) from sort";
		long i = 0;
		try {
			i = qr.query(conn, sql, new ScalarHandler<Long>());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(i);
		// 6.3关流
		DbUtils.closeQuietly(conn);
	}
	
	// 5.采用ColumnListHandler处理结果集,将结果集中指定的列的字段值，封装到一个List集合中
	public static void columnListHandler() {
		// 5.1创建QueryRunner对象
		QueryRunner qr = new QueryRunner();
		// 5.2传参
		String sql = "select * from sort";
		List<Object> list = null;
		try {
			list = qr.query(conn, sql, new ColumnListHandler<Object>("sid"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		for(Object obj : list) {
			System.out.println(obj);
		}
		// 5,3关流
		DbUtils.closeQuietly(conn);
		
	}
	
	// 4.采用BeanListHandler处理结果集,将结果集中每一条记录封装到指定的javaBean中，将这些javaBean在封装到List集合中
	public static void beanListHandler() {
		// 4.1创建QueryRunner对象
		QueryRunner qr = new QueryRunner();
		// 4.2传参
		String sql = "select * from sort";
		List<Sort> list = null;
		try {
			list = qr.query(conn, sql, new BeanListHandler<Sort>(Sort.class));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		for(Sort s : list) {
			System.out.println(s);
		}
		// 4.3关流
		DbUtils.closeQuietly(conn);
	}
	
	
	// 3.采用BeanHandler处理结果集,将结果集中第一条记录封装到一个指定的javaBean中
	public static void beanHandler() {
		// 3.1创建QueryRunner对象
		QueryRunner qr = new QueryRunner();
		// 3.2传参
		String sql = "select * from sort";
		Sort s = null;
		try {
			s = qr.query(conn, sql, new BeanHandler<Sort>(Sort.class));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(s);
		// 3.3关流
		DbUtils.closeQuietly(conn);
	}
	
	
	// 2.采用ArrayListHandler处理结果集,将结果的每行存储到Object[]数组中,然后存到list集合中
	public static void arrListHandler() {
		// 2.1创建QueryRunner对象
		QueryRunner qr = new QueryRunner();
		// 2.2传参
		String sql = "select * from sort";
		List<Object[]> list = null;
		try {
			list = qr.query(conn, sql, new ArrayListHandler());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		for(Object[] objs : list) {
			for(Object obj : objs) {
				System.out.print(obj + "\t");
			}
			System.out.println();
		}
		// 2.3关流
		DbUtils.closeQuietly(conn);
	}
	
	
	
	// 1.采用ArrayHandler处理结果集,将结果的首行存储到Object[]数组中
	public static void arrHandler() {
		// 1.1创建QueryRunner对象
		QueryRunner qr = new QueryRunner();
		// 1.2传参
		String sql = "select * from sort";
		Object[] result = null;
		try {
			result = qr.query(conn, sql, new ArrayHandler());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		for(Object obj : result) {
			System.out.println(obj);
		}
		// 1.3关流
		DbUtils.closeQuietly(conn);
		
	}
	
	
}
