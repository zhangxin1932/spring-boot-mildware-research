package com.zy.spring.mildware.mongodb.config;

import com.mongodb.client.MongoClients;
import com.zy.spring.mildware.mongodb.commons.MongoDbCollection;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

@Configuration
public class MongoConfiguration {

    @Value("${mongodb.uri}")
    private String uri;

    @Value("${mongodb.db1}")
    private String mongoDb1;

    @Value("${mongodb.db2}")
    private String mongoDb2;

    @Value("${mongodb.file_server}")
    private String mongoDbFileServer;

    @Primary
    @Bean(name = "mongoTemplateDb1")
    public MongoTemplate mongoTemplateDb1() {
        return new MongoTemplate(new SimpleMongoClientDatabaseFactory(MongoClients.create(uri), mongoDb1));
    }

    @Bean(name = "mongoTemplateDb2")
    public MongoTemplate mongoTemplateDb2() {
        return new MongoTemplate(new SimpleMongoClientDatabaseFactory(MongoClients.create(uri), mongoDb2));
    }

    @Bean(name = "mongoTemplateDbFileServer")
    public MongoTemplate mongoTemplateDbFileServer() {
        return new MongoTemplate(new SimpleMongoClientDatabaseFactory(MongoClients.create(uri), mongoDbFileServer));
    }

    @Bean(name = "tipsGridFsTemplate")
    public GridFsTemplate tipsGridFsTemplate(@Qualifier("mongoTemplateDbFileServer") MongoTemplate mongoTemplateDbFileServer) {
        return new GridFsTemplate(mongoTemplateDbFileServer.getMongoDatabaseFactory(),
                mongoTemplateDbFileServer.getConverter(),
                MongoDbCollection.BUCKET_TIPS.getName());
    }

    /*@Bean
    public MongoTransactionManager mongoTransactionManager(MongoDatabaseFactory mongoDatabaseFactory) {

    }*/

}
