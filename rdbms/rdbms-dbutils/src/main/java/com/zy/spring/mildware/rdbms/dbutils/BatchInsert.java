package com.zy.spring.mildware.rdbms.dbutils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BatchInsert {

    public static void main(String[] args) {
        for (int i = 1; i <= 10; i++) {
            new Worker().start();
        }
    }

    private static class Worker extends Thread {

        @Override
        public void run() {
            String url = "jdbc:mysql://192.168.0.199/batch_insert";
            String name = "com.mysql.jdbc.Driver";
            String user = "root";
            String password = "123456";
            Connection conn;
            try {
                Class.forName(name);
                conn = DriverManager.getConnection(url, user, password);//获取连接
                conn.setAutoCommit(false);//关闭自动提交，不然conn.commit()运行到这句会报错
            } catch (ClassNotFoundException | SQLException e1) {
                throw new RuntimeException(e1);
            }
            // 开始时间
            long begin = System.currentTimeMillis();
            // sql前缀
            String prefix = "INSERT INTO stu (id,name,lang) VALUES ";
            try {
                // 保存sql后缀
                StringBuffer suffix;
                // 设置事务为非自动提交
                conn.setAutoCommit(false);
                // 比起st，pst会更好些
                PreparedStatement pst = (PreparedStatement) conn.prepareStatement("");//准备执行语句
                // 外层循环，总提交事务次数
                suffix = new StringBuffer();
                for (int i = 1; i <= 10; i++) {
                    // 第j次提交步长
                    for (int j = 1; j <= 100; j++) {
                        // 构建SQL后缀
                        for (int k = 1; k <= 100; k++) {
                            suffix.append("('a" + i + "','b" + j + "','c" + k + "'),");
                        }
                    }
                }
                // 构建完整SQL
                String sql = prefix + suffix.substring(0, suffix.length() - 1);
                // 添加执行SQL
                pst.addBatch(sql);
                // 执行操作
                pst.executeBatch();
                // 提交事务
                conn.commit();
                // 关闭连接
                pst.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // 结束时间
            long end = System.currentTimeMillis();
            // 耗时
            System.out.println("100万条数据插入花费时间 : " + (end - begin) / 1000 + " s" + "  插入完成");
        }
    }

}

