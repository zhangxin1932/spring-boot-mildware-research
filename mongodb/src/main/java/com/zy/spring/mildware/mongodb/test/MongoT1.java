package com.zy.spring.mildware.mongodb.test;

import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import com.zy.spring.mildware.mongodb.commons.MongoTestDbEnum;
import org.bson.Document;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class MongoT1 {

    private static final MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
    private static final MongoDatabase mongoDatabase = mongoClient.getDatabase("test");

    public static void main(String[] args) throws Exception {
        try {
            f2();
        } finally {
            mongoClient.close();
        }
    }

    /**
     * 备注：skip() 、limit()、sort() 三个放在一起执行的时候，执行的顺序是:
     * 先 sort(), 然后是skip(),最后是显示 limit()。
     */
    private static void f2() {
        Pattern pattern = Pattern.compile("不通过|审批中", Pattern.CASE_INSENSITIVE);
        // BasicDBObject query = new BasicDBObject();
        // 1.查找不包含某个正则的数据
        // BasicDBObject query = new BasicDBObject("status", new BasicDBObject("$not", pattern));
        // 2.查找包含某个正则的数据
        // BasicDBObject query = new BasicDBObject("status", new BasicDBObject("$regex", pattern));
        // 3.查找时间小于某个值的记录，这里时间类型是：ISODate，即插入时用的是 new Date();
        // 强烈建议用 Long 型的时间戳
        /*LocalDateTime localDateTime = LocalDateTime.of(2022, 10, 14, 20, 59, 21);
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());*/
        // 这里小时数是 12，减去了8 小时
        LocalDateTime localDateTime = LocalDateTime.of(2022, 10, 14, 12, 59, 21);
        BasicDBObject query = new BasicDBObject("createTime", new BasicDBObject("$lt", localDateTime));

        // 3.排序
        BasicDBObject sort = new BasicDBObject("createTime", -1);
        try (MongoCursor<Document> cursor = mongoDatabase.getCollection(MongoTestDbEnum.db1.getName())
                .find(query)
                .sort(sort)
                .skip(0)
                .limit(3)
                .cursor()) {
            while (cursor.hasNext()) {
                System.out.println("-----------------");
                Document document = cursor.next();
                System.out.println(document);
            }
        }
    }

    private static void f1() throws Exception {
        Document document2 = new Document();
        document2.append("_id", "2")
                .append("status", "审批不通过")
                .append("createTime", new Date());
        TimeUnit.SECONDS.sleep(2);

        Document document = new Document();
        document.append("_id", "1")
                .append("status", "审批通过")
                .append("createTime", new Date());
        TimeUnit.SECONDS.sleep(2);

        Document document3 = new Document();
        document3.append("_id", "3")
                .append("status", "审批中")
                .append("createTime", new Date());

        List<Document> documents = Lists.newArrayList(document, document2, document3);
        mongoDatabase.getCollection(MongoTestDbEnum.db1.getName()).insertMany(documents);
    }

}
