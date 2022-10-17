package com.zy.spring.mildware.solr.controller;

import com.alibaba.fastjson.JSON;
import com.zy.spring.mildware.solr.common.SolrCoreEnum;
import com.zy.spring.mildware.solr.entity.MriEntity;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.json.NestableJsonFacet;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

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
