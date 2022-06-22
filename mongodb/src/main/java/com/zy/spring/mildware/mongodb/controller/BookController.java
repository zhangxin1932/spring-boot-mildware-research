package com.zy.spring.mildware.mongodb.controller;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.zy.spring.mildware.mongodb.commons.MongoDbCollection;
import com.zy.spring.mildware.mongodb.entity.Book;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * https://www.jianshu.com/p/95738dc76927
 */
@RestController
@RequestMapping("/mongodb/book/")
public class BookController {

    @Resource(name = "mongoTemplateDb1")
    private MongoTemplate mongoTemplateDb1;

    @PostMapping("save")
    public void saveObj(@RequestBody List<Book> books) {
        try {
            // BulkOperations.BulkMode.UNORDERED 表示当主键重复错误时, 不影响后续插入操作
            BulkOperations ops = mongoTemplateDb1.bulkOps(BulkOperations.BulkMode.UNORDERED, MongoDbCollection.BOOK.getName());
            ops.insert(books);
            ops.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("------>finish");
    }

    @GetMapping("findAll")
    public List<Book> findAll() {
        return mongoTemplateDb1.findAll(Book.class, MongoDbCollection.BOOK.getName());
    }

    @GetMapping("findOne")
    public Book findOne(@RequestParam String id) {
        final Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        return mongoTemplateDb1.findOne(query, Book.class, MongoDbCollection.BOOK.getName());
    }

    /**
     * 常规查询操作
     * Criteria—条件
     * Sort—排序
     * Query—查询语句
     */
    @GetMapping("findOneByName")
    public Book findOneByName(@RequestParam String name, Pageable pageable) {
        final Query query = new Query();
        query.addCriteria(Criteria.where("name").is(name)); // 先$match筛选
        query.with(Sort.by(Sort.Direction.DESC, "name")) // 在$sort排序
                .with(pageable);
        return mongoTemplateDb1.findOne(query, Book.class, MongoDbCollection.BOOK.getName());
    }

    @PostMapping("update")
    public void update(@RequestBody Book book) {
        Query query = new Query(Criteria.where("_id").is(book.getId()));
        Update update = new Update().
                set("publish", book.getPublish()).
                set("info", book.getInfo()).
                set("updateTime", new Date());
        // 更新查询返回结果集的第一条
        UpdateResult updateResult = mongoTemplateDb1.updateFirst(query, update, Book.class);
    }

    @PostMapping("delOne")
    public void delOne(@RequestBody Book book) {
        DeleteResult result = mongoTemplateDb1.remove(book);
    }

    @GetMapping("delById")
    public void delById(@RequestParam String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        final Book book = mongoTemplateDb1.findOne(query, Book.class);
        DeleteResult result = mongoTemplateDb1.remove(book);
    }
}
