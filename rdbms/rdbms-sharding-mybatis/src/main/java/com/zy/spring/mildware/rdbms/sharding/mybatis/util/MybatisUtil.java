/*
package com.zy.spring.mildware.rdbms.sharding.mybatis.util;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

public class MybatisUtil {

    private static final ThreadLocal<SqlSession> THREAD_LOCAL = new ThreadLocal<>();
    private static final SqlSessionFactory SQL_SESSION_FACTORY;

    static {
        InputStream is = MybatisUtil.class.getClassLoader().getResourceAsStream("sqlMappingConfig.xml");
        SQL_SESSION_FACTORY = new SqlSessionFactoryBuilder().build(is);
    }

    */
/*定义或者sqlSession的方法*//*

    public static SqlSession getSqlSession() {
        SqlSession sqlSession = THREAD_LOCAL.get();
        if (sqlSession == null) {
            sqlSession = SQL_SESSION_FACTORY.openSession(true);
            THREAD_LOCAL.set(sqlSession);
        }
        return sqlSession;
    }

    */
/*定义关闭方法*//*

    public static void closeSqlSession(){
        SqlSession sqlSession = THREAD_LOCAL.get();
        if(sqlSession != null){
            sqlSession.close();
            THREAD_LOCAL.remove();
        }
    }
}
*/
