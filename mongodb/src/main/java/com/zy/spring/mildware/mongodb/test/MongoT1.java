package com.zy.spring.mildware.mongodb.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import com.zy.spring.mildware.mongodb.commons.MongoTestDbEnum;
import com.zy.spring.mildware.mongodb.config.DefaultWebClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.bson.Document;
import org.bson.types.Decimal128;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MongoT1 {

    private static final MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
    private static final MongoDatabase mongoDatabase = mongoClient.getDatabase("test");
    private static final HttpSolrClient solrClient = new HttpSolrClient.Builder()
            .withBaseSolrUrl("http://10.102.224.206:8983/solr")
            .build();

    public static void main(String[] args) throws Exception {
        try {
            /*solrClient.deleteByQuery("c1", "*:*");
            solrClient.commit("c1");*/
            f5();
        } finally {
            mongoClient.close();
        }
    }

    private static void f5() throws Exception {
        try (MongoCursor<Document> cursor = mongoDatabase.getCollection(MongoTestDbEnum.mri.getName()).find().cursor();) {
            while (cursor.hasNext()) {
                SolrInputDocument document = new SolrInputDocument();
                Document next = cursor.next();
                String dateOfOutcome = next.getString("dateOfOutcome");
                dateOfOutcome = Objects.isNull(dateOfOutcome) ? null : dateOfOutcome.substring(0, 10);
                document.setField("id", next.getString("_id"));
                document.setField("maHolder", next.getString("maHolder"));
                document.setField("name", next.getString("name"));
                document.setField("outcome", next.getString("outcome"));
                document.setField("domainKey", next.getString("domainKey"));
                document.setField("dateOfOutcome", dateOfOutcome);

                List<Document> doseForms = next.getList("doseForms", Document.class);
                if (!CollectionUtils.isEmpty(doseForms)) {
                    doseForms.stream().filter(Objects::nonNull).forEach(result -> {
                        // Long sporId = result.getLong("sporId");
                        String doseFormName = result.getString("doseFormName");
                    });
                }

                List<Document> activeSubstances = next.getList("activeSubstances", Document.class);
                if (!CollectionUtils.isEmpty(activeSubstances)) {
                    activeSubstances.stream().filter(Objects::nonNull).forEach(result -> {
                        String innName = result.getString("innName");
                        Decimal128 amount = result.get("amount", Decimal128.class);
                        String saltName = result.getString("saltName");
                    });
                }

                List<Document> documents = next.getList("documents", Document.class);
                if (!CollectionUtils.isEmpty(documents)) {
                    documents.stream().filter(Objects::nonNull).forEach(result -> {
                        String documentType = result.getString("documentType");
                    });
                }

                List<Document> cms = next.getList("cms", Document.class);
                if (!CollectionUtils.isEmpty(cms)) {
                    cms.stream().filter(Objects::nonNull).forEach(result -> {
                        // Long sporId = result.getLong("sporId");
                        String isoCode = result.getString("isoCode");
                        String countryName = result.getString("countryName");
                    });
                }

                List<Document> atcCodes = next.getList("atcCodes", Document.class);
                if (!CollectionUtils.isEmpty(atcCodes)) {
                    Set<Long> sporIds = Sets.newHashSet();
                    Set<String> codes = Sets.newHashSet();
                    Set<String> atcCodeNames = Sets.newHashSet();
                    atcCodes.stream().filter(Objects::nonNull).forEach(result -> {
                        sporIds.add(result.getLong("sporId"));
                        codes.add(result.getString("code"));
                        atcCodeNames.add(result.getString("atcCodeName"));
                    });
                    document.setField("atcCodeSporIds", sporIds.stream().filter(Objects::nonNull).collect(Collectors.toSet()));
                    document.setField("atcCodes", codes.stream().filter(Objects::nonNull).collect(Collectors.toSet()));
                    document.setField("atcCodeNames", atcCodeNames.stream().filter(Objects::nonNull).collect(Collectors.toSet()));
                }
                solrClient.add("c1", document);
            }
        } catch (Exception e) {
            System.out.println("msg is:" + e.getCause());
        }
        solrClient.commit("c1");
    }

    private static void f4() {
        // String url = "https://mri.cts-mrp.eu/portal/v1/odata/ProductSearch?$filter=(UpdatedAt%20ge%202020-01-01T00%3A00%3A00%2B08%3A00)%20and%20(UpdatedAt%20le%202022-10-22T10%3A09%3A22%2B08%3A00)%20and%20(domainKey%20eq%20%27h%27)&$expand=activeSubstances,atcCodes,cms,documents,doseForms,rms,typeLevel,withdrawalReasons&$count=true&$skip=0&$top=1000";
        int skip = 0;
        int count = 0;
        while (skip < 36000) {
            String url = "https://mri.cts-mrp.eu/portal/v1/odata/ProductSearch?$filter=(UpdatedAt%20ge%202020-01-01T00%3A00%3A00%2B08%3A00)%20and%20(UpdatedAt%20le%202022-10-22T10%3A09%3A22%2B08%3A00)%20and%20(domainKey%20eq%20%27h%27)&$expand=activeSubstances,atcCodes,cms,documents,doseForms,rms,typeLevel,withdrawalReasons&$count=true&$skip=" + skip + "&$top=100";
            String body = null;
            try {
                body = DefaultWebClient.getInstance()
                        .getAbs(url)
                        .putHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:96.0) Gecko/20100101 Firefox/96.0")
                        .rxSend()
                        .blockingGet()
                        .bodyAsString(StandardCharsets.UTF_8.name());
            } catch (Exception e) {
                System.out.println("count -----------> " + count);
                continue;
            }
            JSONObject jsonObject = JSON.parseObject(body);
            JSONArray value = jsonObject.getJSONArray("value");
            for (Object obj : value) {
                JSONObject result = (JSONObject) obj;
                String productKey = result.getString("productKey");
                if (Objects.isNull(productKey) || Objects.equals("", productKey.trim())) {
                    continue;
                }
                Document document = new Document();
                document.putAll(result);
                document.put("_id", productKey);
                try {
                    mongoDatabase.getCollection(MongoTestDbEnum.mri.getName()).insertOne(document);
                    count++;
                } catch (Exception e) {
                    System.out.printf("id:{%s}, msg:{%s}.%n", productKey, e.getMessage());
                }
            }
            skip += 100;
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
        // 强烈建议用 Long 型的时间戳：也方便跨语言使用
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
