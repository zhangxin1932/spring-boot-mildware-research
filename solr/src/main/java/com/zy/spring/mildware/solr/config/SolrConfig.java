package com.zy.spring.mildware.solr.config;

import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SolrConfig {

    @Bean
    public HttpSolrClient solrClient() {
        return new HttpSolrClient.Builder()
                .withBaseSolrUrl("http://10.102.224.206:8983/solr")
                .build();
    }

}
