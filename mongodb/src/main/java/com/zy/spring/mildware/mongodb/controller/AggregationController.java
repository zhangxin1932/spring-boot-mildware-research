package com.zy.spring.mildware.mongodb.controller;

import com.alibaba.fastjson.JSON;
import com.mongodb.BasicDBObject;
import com.zy.spring.mildware.mongodb.commons.MongoDbCollection;
import com.zy.spring.mildware.mongodb.entity.Items;
import com.zy.spring.mildware.mongodb.entity.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 聚合函数的原生语法：
 * db.collection.aggregate(pipeline,options);
 *
 * pipeline一系列的聚合操作，可以对文档进行一连串的处理，
 * 包括筛选（match）、映射（project）、分组（group）、排序（sort）、限制（limit）、跳跃（skip）。
 * options（可选）控制聚合函数的执行的一些参数。
 *
 * 而org.springframework.data.mongodb.core.aggregation.Aggregation提供了上述4个newAggregation方法，来接收pipeline数组。即AggregationOperation集合。
 *
 */
@RestController
@RequestMapping("/mongo/aggregation/")
@Slf4j
public class AggregationController {

    @Resource(name = "mongoTemplateDb2")
    private MongoTemplate mongoTemplateDb2;

    @RequestMapping("queryStudents")
    public List<BasicDBObject> queryStudents() {
        TypedAggregation<Student> typedAggregation = Aggregation.newAggregation(Student.class,
                Aggregation.match(Criteria.where("scope").gt(60).lt(90))  //相当于where操作
        );
        //打印出原生的聚合命令
        log.info("输出聚合对象{}", typedAggregation.toString());
        AggregationResults<BasicDBObject> aggregate = mongoTemplateDb2.aggregate(typedAggregation, MongoDbCollection.STUDENTS.getName(), BasicDBObject.class);
        //输出结果
        List<BasicDBObject> mappedResults = aggregate.getMappedResults();
        log.info("输出结果{}", mappedResults.toString());
        return mappedResults;
    }

    /**
     * 最终会在Mongodb中执行如下代码：
     *
     * db.items.aggregate(
     * [{ "$match" : { "price" : { "$in" : [10.0, 5.0] } } }, //使用in去筛选
     * { "$project" : { "date" : 1, "month" : { "$month" : "$date" } } }, //映射数据
     * { "$group" : { "_id" : "$month", "num" : { "$sum" : 1 } } }, //以映射的数据分组，在进行统计
     * { "$project" : { "num" : 1, "_id" : 0, "month" : "$_id" } }] //打印统计数据和month月份信息
     * )
     *
     * @return
     */
    @RequestMapping("queryItems")
    public List<BasicDBObject> queryItems() {
        List<Double> collection = new ArrayList<>();
        collection.add(10.0);
        collection.add(5.0);
        TypedAggregation<Items> itemsTypedAggregation = Aggregation.newAggregation(Items.class,
                Aggregation.match(Criteria.where("price").in(collection)),
                Aggregation.project("date").andExpression("month(date)").as("month"),
                Aggregation.group("month").count().as("num"),
                Aggregation.project("num").and("month").previousOperation()
        );
        //打印表达式
        log.info("聚合表达式：{}",itemsTypedAggregation.toString());
        AggregationResults<BasicDBObject> result = mongoTemplateDb2.aggregate(itemsTypedAggregation, MongoDbCollection.ITEMS.getName(), BasicDBObject.class);
        //打印数据
        log.info(JSON.toJSONString(result.getMappedResults()));
        return result.getMappedResults();
    }

    /**
     * 此处有坑, 注意一下
     *
     * 按date的月份、日期、年份对文档进行分组，并计算total price和average quantity，并汇总各文档的数量。
     * @return
     */
    @RequestMapping("groupItems")
    public List<BasicDBObject> groupItems() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.project("quantity")
                        .andExpression("year(date)").as("year")
                        .andExpression("month(date)").as("month")
                        .andExpression("dayOfMonth(date)").as("day")
                        .andExpression("price * quantity").as("grossPrice"),
                //     .and("quantity"), 等效于project("quantity")
                Aggregation.group("month", "year", "day")
                        .sum("grossPrice").as("totalPrice")
                        .avg("quantity").as("averageQuantity")
                        .count().as("count")
        );
        log.info("聚合表达式：{}", aggregation);
        AggregationResults<BasicDBObject> items = mongoTemplateDb2.aggregate(aggregation, MongoDbCollection.ITEMS.getName(), BasicDBObject.class);
        log.info(JSON.toJSONString(items.getMappedResults()));
        return items.getMappedResults();
    }
}
