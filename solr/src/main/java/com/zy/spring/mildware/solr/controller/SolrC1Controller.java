package com.zy.spring.mildware.solr.controller;

import com.zy.spring.mildware.solr.common.SolrCoreEnum;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/c1/")
public class SolrC1Controller {
    @Autowired
    private HttpSolrClient solrClient;

    public void insert() throws Exception {
        SolrInputDocument document = new SolrInputDocument();
        solrClient.add(SolrCoreEnum.c1.name(), document);
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
