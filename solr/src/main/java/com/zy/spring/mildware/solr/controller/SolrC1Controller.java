package com.zy.spring.mildware.solr.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zy.spring.mildware.solr.common.SolrCoreEnum;
import com.zy.spring.mildware.solr.entity.MriEntity;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.json.*;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.json.BucketBasedJsonFacet;
import org.apache.solr.client.solrj.response.json.BucketJsonFacet;
import org.apache.solr.client.solrj.response.json.NestableJsonFacet;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.util.NamedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequestMapping("/c1/")
public class SolrC1Controller {
    @Autowired
    private HttpSolrClient solrClient;

    @RequestMapping("insert")
    public void insert() throws Exception {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("mri.json");
        List<MriEntity> mriEntities = JSON.parseArray(IOUtils.toString(inputStream, StandardCharsets.UTF_8), MriEntity.class);

        for (MriEntity mriEntity : mriEntities) {
            SolrInputDocument document = new SolrInputDocument();
            document.setField("id", mriEntity.getProductKey());
            document.setField("apiId", mriEntity.getApiId());
            document.setField("productKey", mriEntity.getProductKey());
            document.setField("name", mriEntity.getName());
            document.setField("domainKey", mriEntity.getDomainKey());
            document.setField("maHolder", mriEntity.getMaHolder());
            document.setField("dateOfOutcome", mriEntity.getDateOfOutcome());
            document.setField("publishAt", mriEntity.getPublishAt());
            document.setField("updatedAt", mriEntity.getUpdatedAt());
            solrClient.add(SolrCoreEnum.c1.name(), document);
        }

        solrClient.commit(SolrCoreEnum.c1.name());
    }

    /**
     * https://www.thinbug.com/q/39891895
     * https://blog.csdn.net/zteny/article/details/73731958
     * json.facet
     * facet
     *
     * @return
     */
    @RequestMapping("query")
    public Object query() throws Exception {
        SolrQuery query = new SolrQuery();
        query.setFacet(true);
        query.setFacetLimit(-1);
        query.setFacetMinCount(1);
        QueryResponse response = solrClient.query(SolrCoreEnum.c1.name(), query);
        NestableJsonFacet jsonFacetingResponse = response.getJsonFacetingResponse();
        return response;
    }

    /**
     * https://solr.apache.org/guide/8_3/json-facet-api.html
     *
     * 这里的数据，参见：{@link com.zy.spring.mildware.mongodb.test.MongoT1#f5()}
     * @return
     * @throws Exception
     */
    @RequestMapping("jsonFacetQuery")
    public Object jsonFacetQuery(String fn) throws Exception {
        if (Objects.equals(fn, "f1")) {
            return f1();
        } else if (Objects.equals(fn, "f2")) {
            return f2();
        } else if (Objects.equals(fn, "f3")) {
            return f3();
        } else if (Objects.equals(fn, "f4")) {
            return f4();
        }
        return null;
    }

    private Object f4() throws Exception {
        final JsonQueryRequest request = new JsonQueryRequest();
        // outcome 不是 Positive 的
        request.setQuery("-outcome:Positive");
        // fl 参数
        request.returnFields("id", "name", "outcome", "dateOfOutcome");
        // 偏移量
        request.setOffset(0);
        // 分页数量
        request.setLimit(5);
        // 排序
        request.setSort("dateOfOutcome desc");
        QueryResponse queryResponse = request.process(solrClient, SolrCoreEnum.c1.name());
        // 结果集
        SolrDocumentList results = queryResponse.getResults();
        // 结果总数: 可以参考 solr 管理台的返回的数据结构来通过代码获取对应的属性值
        long numFound = results.getNumFound();
        return results;
    }

    /**
     * {"query":"*:*","limit":0,"facet":{"atcCodeFacet":{"field":"atcCodes","mincount":7,"limit":5,"type":"terms","facet":{"count_outcome":"unique(outcome)"}},"maHolderFacet":{"field":"maHolder","mincount":8,"limit":5,"sort":"count desc","type":"terms"},"by_id":{"q":"outcome:Positive","type":"query","facet":{"by_id_count":"unique(id)"}}}}
     *
     * {
     *     "query": "*:*",
     *     "limit": 0,
     *     "facet": {
     *         "atcCodeFacet": {
     *             "field": "atcCodes",
     *             "mincount": 7,
     *             "limit": 5,
     *             "type": "terms",
     *             "facet": {
     *                 "count_outcome": "unique(outcome)"
     *             }
     *         },
     *         "maHolderFacet": {
     *             "field": "maHolder",
     *             "mincount": 8,
     *             "limit": 5,
     *             "sort": "count desc",
     *             "type": "terms"
     *         },
     *         "by_id": {
     *             "q": "outcome:Positive",
     *             "type": "query",
     *             "facet": {
     *                 "by_id_count": "unique(id)"
     *             }
     *         }
     *     }
     * }
     *
     * @return
     * @throws Exception
     */
    private Object f3() throws Exception {
        final JsonQueryRequest request = new JsonQueryRequest()
                .setQuery("*:*")
                .setLimit(0)
                .withFacet("atcCodeFacet",
                        new TermsFacetMap("atcCodes").setLimit(5).setMinCount(7)
                                .withStatSubFacet("count_outcome", "unique(outcome)")
                )
                .withFacet("maHolderFacet",
                        new TermsFacetMap("maHolder").setLimit(5).setMinCount(8).setSort("count desc")
                )
                .withFacet("by_id", new QueryFacetMap("outcome:Positive")
                        .withStatSubFacet("by_id_count", "unique(id)")
                )
                /*.withFacet("dateOfOutcomeFacet",
                        new RangeFacetMap("dateOfOutcome", new Date(2021, 10, 1), new Date(2022, 10, 1), "+1MONTH")
                )*/
                ;
        return parse(request);
    }

    private Object parse(JsonQueryRequest request) throws Exception {
        QueryResponse queryResponse = request.process(solrClient, SolrCoreEnum.c1.name());
        SolrDocumentList results = queryResponse.getResults();
        NestableJsonFacet jsonFacetingResponse = queryResponse.getJsonFacetingResponse();
        Set<String> bucketBasedFacetNames = jsonFacetingResponse.getBucketBasedFacetNames();
        Set<String> statNames = jsonFacetingResponse.getStatNames();

        List<JSONObject> finalResp = Lists.newArrayList();

        Set<String> queryFacetNames = jsonFacetingResponse.getQueryFacetNames();
        queryFacetNames.forEach(queryFacetName -> {
            NestableJsonFacet queryFacet = jsonFacetingResponse.getQueryFacet(queryFacetName);
            long count = queryFacet.getCount();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(queryFacetName, count);
            Set<String> statNamesSet = queryFacet.getStatNames();
            if (!CollectionUtils.isEmpty(statNamesSet)) {
                statNamesSet.forEach(e -> {
                    jsonObject.put(e, queryFacet.getStatValue(e));
                });
            }
            finalResp.add(jsonObject);
        });

        bucketBasedFacetNames.forEach(bucketBasedFacetName -> {
            BucketBasedJsonFacet bucketBasedFacets = jsonFacetingResponse.getBucketBasedFacets(bucketBasedFacetName);
            List<BucketJsonFacet> buckets = bucketBasedFacets.getBuckets();
            List<JSONObject> resp = Lists.newArrayList();
            for (BucketJsonFacet bucket : buckets) {
                JSONObject jsonObject = new JSONObject();
                Object val = bucket.getVal();
                if (Objects.nonNull(val)) {
                    String value = val.toString();
                    jsonObject.put(value, bucket.getCount());
                    Set<String> statNamesSet = bucket.getStatNames();
                    if (!CollectionUtils.isEmpty(statNamesSet)) {
                        statNamesSet.forEach(e -> {
                            jsonObject.put(e, bucket.getStatValue(e));
                        });
                    }
                }
                resp.add(jsonObject);
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(bucketBasedFacetName, resp);
            finalResp.add(jsonObject);
        });
        return finalResp;
    }

    private Object f1() throws Exception {
        TermsFacetMap termsFacetMap = new TermsFacetMap("name");
        termsFacetMap.setLimit(5);
        termsFacetMap.setMinCount(7);
        // termsFacetMap.setSort("desc");
        final JsonQueryRequest request = new JsonQueryRequest()
                .setQuery("*:*")
                .setLimit(0)
                .withFacet("categories", termsFacetMap);
        return parse(request);
    }

    private Object f2() throws Exception {
        final TermsFacetMap nameFacet = new TermsFacetMap("name")
                .setLimit(2);
        final TermsFacetMap allManufacturersFacet = new TermsFacetMap("manu_id_s")
                .setLimit(2)
                //.withDomain(new DomainMap().withTagsToExclude("MANU"))
                ;
        final JsonQueryRequest request = new JsonQueryRequest()
                //.setQuery("cat:electronics")
                .setQuery("*:*")
                //.withFilter("{!tag=MANU}manu_id_s:apple")
                .withFacet("name", nameFacet)
                .withFacet("manufacturers", allManufacturersFacet);
        return parse(request);
    }

    @RequestMapping("getHmaData")
    public String getHmaData(Integer size) throws Exception {
        // http://localhost:8080/c1/getHmaData?size=10
        // HttpGet httpGet = new HttpGet("https://mri.cts-mrp.eu/portal/v1/odata/ProductSearch?$filter=(UpdatedAt ge 2022-09-16T22:00:21+08:00) and (UpdatedAt le 2022-10-16T22:00:21+08:00) and (domainKey eq 'h')&$expand=activeSubstances,atcCodes,cms,documents,doseForms,rms,typeLevel,withdrawalReasons&$count=true&$skip=0&$top=" + size);
        HttpGet httpGet = new HttpGet("https://mri.cts-mrp.eu/portal/v1/odata/ProductSearch?$filter=" + URLEncoder.encode("(UpdatedAt ge 2022-09-16T22:00:21+08:00) and (UpdatedAt le 2022-10-16T22:00:21+08:00) and (domainKey eq 'h')", StandardCharsets.UTF_8.name()) + "&$expand=activeSubstances,atcCodes,cms,documents,doseForms,rms,typeLevel,withdrawalReasons&$count=true&$skip=0&$top=" + size);
        HttpResponse response = solrClient.getHttpClient().execute(httpGet);
        String body = EntityUtils.toString(response.getEntity());
        System.out.println(body);
        return body;
    }

}
