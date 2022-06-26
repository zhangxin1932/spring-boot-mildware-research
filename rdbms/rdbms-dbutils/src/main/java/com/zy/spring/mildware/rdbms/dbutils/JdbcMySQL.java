package com.zy.spring.mildware.rdbms.dbutils;

import java.sql.*;
import java.util.Scanner;


/*	JDBC开发步骤
1.	注册驱动.
2.	获得连接.
3.	获得语句执行平台
4.	执行sql语句
5.	处理结果
6.	释放资源.
*
 * 0.导入驱动jar包
 * 创建lib目录，用于存放当前项目需要的所有jar包
 * 选择jar包，右键执行build path / Add to Build Path
*
*/

public class JdbcMySQL {

	public static void main(String[] args) throws SQLException {
		Connection conn = JdbcMysqlUtil.getConnection("localhost", 3306, "fish", "root", "123456");
		// 3.获取执行SQL语句的对象
		Statement st = conn.createStatement();
		// 4.执行SQL语句
		String sql = "select * from sort";
		ResultSet rs = st.executeQuery(sql);
		// 5.处理结果集
		while(rs.next()) {
			// 通过getXXX方法获得,建议方法中写String类型的列名
			// getObject(),getString()方法可以获取所有类型
			System.out.println(rs.getInt("sid") + "\t" + rs.getString("sname")
				+ "\t" + rs.getDouble("sprice") + "\t" + rs.getString("sdesc")
					);
		}
		// 6.关流
		JdbcMysqlUtil.close(conn, st, rs);
		
	}

	// 4.jdbc防止注入攻击:
	public static void antiAttach() throws ClassNotFoundException, SQLException {
		Scanner sc = new Scanner(System.in);
		// 1.注册驱动
		Class.forName("com.mysql.jdbc.Driver");
		// 2.获得数据库的连接
		String url = "jdbc:mysql://localhost:3306/fish";
		String u = "root";
		String p = "123456";
		Connection conn = DriverManager.getConnection(url,u,p);
		// 3.创建执行SQL语句的对象,其中?是占位符
		String sql = "select * from user where user=? and pwd=?";
		PreparedStatement ps = conn.prepareStatement(sql);
		// 4.执行SQL语句
		System.out.println("请输入用户名:");
		String user = sc.nextLine();
		System.out.println("请输入密码:");
		String pwd = sc.nextLine();
		ps.setObject(1,user);
		ps.setObject(2,pwd);
		// 5.处理SQL语句
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			System.out.println(rs.getString("user") + "\t" + rs.getString("pwd"));
		}
		// 6.关流
		rs.close();
		ps.close();
		conn.close();
	}
	
	// 3.jdbc中的注入攻击:
	/* 按下述格式输入,即可获取所有用户名,密码
	 * 请输入用户名:
		m
		请输入密码:
		n' or '1=1*/
	public static void attachJDBC() throws ClassNotFoundException, SQLException {
		Scanner sc = new Scanner(System.in);
		// 1.注册驱动
		Class.forName("com.mysql.jdbc.Driver");
		// 2.获得数据库的连接
		String url = "jdbc:mysql://localhost:3306/fish";
		String u = "root";
		String p = "123456";
		Connection conn = DriverManager.getConnection(url, u, p);
		// 3.获取执行SQL的对象
		Statement st = conn.createStatement();
		// 请输入用户名:
		System.out.println("请输入用户名:");
		String user = sc.nextLine();
		System.out.println("请输入密码:");
		String pwd = sc.nextLine();
		// 4.开始执行SQL语句
		String sql = "SELECT * from user where user='" + user + "' and pwd='" + pwd + "'"; 
		ResultSet rs = st.executeQuery(sql);
		// 5.处理SQL语句
		while(rs.next()) {
			System.out.println(rs.getString("user") + "\t" + rs.getString("pwd"));
		}
		// 6.关流
		rs.close();
		st.close();
		conn.close();
	}
	
	// 2.jdbc中sql语句的"查"
	public static void selectJDBC() throws ClassNotFoundException, SQLException {
		// 1.注册驱动
		Class.forName("com.mysql.jdbc.Driver");
		// 2.获得数据库的连接
		String url = "jdbc:mysql://localhost:3306/fish";
		String user = "root";
		String pwd = "123456";
		Connection conn = DriverManager.getConnection(url,user,pwd);
		// 3.获取执行SQL语句的对象
		Statement st = conn.createStatement();
		// 4.执行SQL语句
		String sql = "select * from sort";
		ResultSet rs = st.executeQuery(sql);
		// 5.处理结果集
		while(rs.next()) {
			// 通过getXXX方法获得,建议方法中写String类型的列名
			// getObject(),getString()方法可以获取所有类型
			System.out.println(rs.getInt("sid") + "\t" + rs.getString("sname")
				+ "\t" + rs.getDouble("sprice") + "\t" + rs.getString("sdesc")
					);
		}
		// 6.关流
		rs.close();
		st.close();
		conn.close();
	}
	
	// 1.jdbc中sql语句的"增","删","改"
	public static void insertJDBC() throws ClassNotFoundException, SQLException {
		// 1.注册驱动
		Class.forName("com.mysql.jdbc.Driver");
		// 2.获得数据库的连接
		/*static Connection		getConnection(String url, String user, String password) 
		尝试建立与给定数据库URL的连接。*/
		/*jdbc:mysql://localhost:3306/mydb
			JDBC规定url的格式由三部分组成，每个部分中间使用冒号分隔。
				第一部分是jdbc，这是固定的；
				第二部分是数据库名称，那么连接mysql数据库，第二部分当然是mysql了；
				第三部分是由数据库厂商规定的，我们需要了解每个数据库厂商的要求，
				mysql的第三部分分别由数据库服务器的IP地址（localhost）、端口号（3306），
				以及DATABASE名称(mydb)组成。*/
		String url = "jdbc:mysql://localhost:3306/fish";
		String user = "root";
		String pwd = "123456";
		Connection conn = DriverManager.getConnection(url, user, pwd);
		System.out.println(conn);
		// 3.获取sql语句执行平台
		/*Statement 		createStatement() 
		创建一个 Statement对象，用于将SQL语句发送到数据库。*/  
		Statement st = conn.createStatement();
		// 4.执行SQL语句
		/*int executeUpdate(String sql) 
		执行给定的SQL语句，这可能是 INSERT ， UPDATE ，或 DELETE语句，
		或者不返回任何内容，如SQL DDL语句的SQL语句。 */ 
		int i = st.executeUpdate("insert into sort(sname,sprice,sdesc) values('鼠标',20,'三星')");
		// 5.处理结果集
		System.out.println(i);
		// 6.释放资源
		st.close();
		conn.close();

	}

}
