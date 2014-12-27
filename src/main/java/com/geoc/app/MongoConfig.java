package com.geoc.app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;

@Configuration
@EnableMongoRepositories("com.geoc.app.repository")
public class MongoConfig extends AbstractMongoConfiguration
{
@Value("${mongodb.name}")
private String  dbName;

@Value("${mongodb.host}")
private String  host;

@Value("${mongodb.port}")
private Integer port;

@Value("${mongodb.username}")
private String  userName;

@Value("${mongodb.password}")
private String  password;


@Override
protected String getDatabaseName()
{
    return this.dbName;
}

@Override
public Mongo mongo() throws Exception
{
    return new MongoClient(this.host, this.port);
}

@Override
@Bean
public SimpleMongoDbFactory mongoDbFactory() throws Exception
{
    return new SimpleMongoDbFactory(mongo(), getDatabaseName());
}

@Override
@Bean
public MongoTemplate mongoTemplate() throws Exception
{
    final UserCredentials userCredentials = new UserCredentials(this.userName, this.password);

    final MongoTemplate mongoTemplate = new MongoTemplate(mongo(), getDatabaseName(), userCredentials);
    mongoTemplate.setWriteConcern(WriteConcern.SAFE);

    return mongoTemplate;
}

}