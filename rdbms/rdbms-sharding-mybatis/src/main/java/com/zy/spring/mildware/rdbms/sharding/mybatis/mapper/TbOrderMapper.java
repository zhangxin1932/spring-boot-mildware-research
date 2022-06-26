package com.zy.spring.mildware.rdbms.sharding.mybatis.mapper;

import com.zy.spring.mildware.rdbms.sharding.mybatis.entity.TbOrder;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * https://github.com/apache/shardingsphere-example/tree/dev/sharding-jdbc-example/sharding-example/sharding-spring-boot-mybatis-example/src/main/resources
 * https://segmentfault.com/a/1190000023828622
 * https://blog.csdn.net/weixin_36338164/article/details/111922857
 */
@Repository
public interface TbOrderMapper {

    int insert(@Param("orderName") String orderName);

    TbOrder selectOrderById(@Param("orderId") Long orderId);

    List<TbOrder> selectOrders();
}
