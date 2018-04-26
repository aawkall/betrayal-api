package com.github.aawkall.betrayalapi.config;

import java.io.IOException;
import java.net.UnknownHostException;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.github.aawkall.betrayalapi.util.BetrayalConst;
import com.mongodb.MongoClient;

import cz.jirutka.spring.embedmongo.EmbeddedMongoBuilder;

@Configuration
@ComponentScan(basePackages = {"com.github.aawkall.betrayalapi.service"})
@EnableMongoRepositories(basePackages = "com.github.aawkall.betrayalapi.repository")
public class TestContext {

	@Bean(destroyMethod = "close", name = "mongoClient")
	public MongoClient mongoClient() throws IOException {
		return new EmbeddedMongoBuilder()
				.version("2.8.0-rc5")
				.bindIp("127.0.0.1")
				.port(12345)
				.build();
	}

	@Inject
	@Bean
	public MongoDbFactory mongoDbFactory(final MongoClient mongoClient) throws UnknownHostException {
		return new SimpleMongoDbFactory(mongoClient, BetrayalConst.DB_NAME);
	}

	@Inject
	@Bean
	public MongoTemplate mongoTemplate(final MongoDbFactory mongoDbFactory) {
		return new MongoTemplate(mongoDbFactory);
	}
}