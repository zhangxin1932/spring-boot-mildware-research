package com.zy.spring.mildware.rdbms.multi.datasource.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.zy.spring.mildware.rdbms.multi.datasource.mapper.master",sqlSessionFactoryRef = "masterSqlSessionFactory")
public class MasterDataSourceConfig {
    @Primary
    @Bean(name = "masterDataSource")
    @ConfigurationProperties("datasource.master")
    public DataSource masterDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "masterSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("masterDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath*:mapper/master/*.xml"));
        return sessionFactoryBean.getObject();
    }

    @Bean
    public PlatformTransactionManager masterTransactionManager(@Qualifier("masterDataSource") DataSource masterDataSource) throws Exception {
        return new DataSourceTransactionManager(masterDataSource);
    }

}
