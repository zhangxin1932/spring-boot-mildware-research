package com.zy.spring.mildware.rdbms.dbutils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class MysqlDeadLock {

    public static void main(String[] args) throws Exception {
        String URL = "jdbc:mysql://127.0.0.1:3306/springcloud?characterEncoding=utf-8";
        String USER = "root";
        String PASSWORD = "123456";
        // 1.加载驱动程序
        Class.forName("com.mysql.jdbc.Driver");
        // 2.获得数据库链接
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        conn.setAutoCommit(false);
        // 3.通过数据库的连接操作数据库，实现增删改查（使用Statement类）
        // 预编译
        String sql = "update tb_stu set name = ? where id = ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, "tom1");
        statement.setLong(2, 1);

        int i = statement.executeUpdate();
        System.out.println(i);
        conn.rollback();
        // 关闭资源
        statement.close();
        conn.close();
    }
}
