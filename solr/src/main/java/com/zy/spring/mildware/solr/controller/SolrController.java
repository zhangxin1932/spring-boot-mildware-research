package com.zy.spring.mildware.solr.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.util.NamedList;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RestController
@RequestMapping("/solr/")
@Slf4j
public class SolrController {

    @Resource
    private HttpSolrClient solrClient;

    @RequestMapping("getDataByCollection")
    public Object getDataByCollection(String collection) {
        try {
            SolrQuery query = new SolrQuery();
            // setQuery 只能设置一次, 后设置的会覆盖先设置的
            // query.setQuery("id:4");
            query.setQuery("*:*");
            query.setStart(0);
            query.setRows(100);
            query.set("fl", "id,userName,address");
            query.set("wt", "json");
            QueryResponse response = solrClient.query(collection, query);
            if (Objects.isNull(response)) {
                return null;
            }
            String jsonStr = response.jsonStr();
            SolrDocumentList results = response.getResults();
            return results;
        } catch (Exception e) {
            log.error("failed to query from solr, collection is:{}.", collection, e);
            return null;
        }
    }

    @RequestMapping("deleteById")
    public Object deleteById(String collection, String id) {
        try {
            UpdateResponse response = solrClient.deleteById(collection, id);
            // 很关键的一句
            solrClient.commit(collection);
            if (Objects.isNull(response)) {
                return null;
            }
            NamedList<Object> resp = response.getResponse();
            return response.jsonStr();
        } catch (Exception e) {
            log.error("failed to deleteById from solr, collection is:{}, id is:{}.", collection, id, e);
        }
        return null;
    }

    @RequestMapping("addDocument")
    public Object addDocument(String collection, HttpServletRequest request) {
        try {
            SolrInputDocument document = new SolrInputDocument();
            document.setField("id", request.getParameter("id"));
            document.setField("userName", request.getParameter("userName"));
            document.setField("address", request.getParameterValues("address"));
            UpdateResponse response = solrClient.add(collection, document);
            solrClient.commit(collection);
            if (Objects.isNull(response)) {
                return null;
            }
            NamedList<Object> resp = response.getResponse();
            return response.jsonStr();
        } catch (Exception e) {
            log.error("failed to addDocument from solr, collection is:{}.", collection, e);
        }
        return null;
    }

}
