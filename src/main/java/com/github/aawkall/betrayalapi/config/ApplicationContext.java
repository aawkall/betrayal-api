package com.github.aawkall.betrayalapi.config;

import java.net.UnknownHostException;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.github.aawkall.betrayalapi.mapper.BetrayalMapperFactory;
import com.github.aawkall.betrayalapi.util.BetrayalConst;
import com.mongodb.MongoClient;

import ma.glasnost.orika.MapperFacade;

@Configuration
@ComponentScan(basePackages = { "com.github.aawkall.betrayalapi.service",
		"com.github.aawkall.betrayalapi.resource", "com.github.aawkall.betrayalapi.mapper" })
@EnableMongoRepositories(basePackages = "com.github.aawkall.betrayalapi.repository")
public class ApplicationContext {

	@Inject
	private Environment env;

	@Inject
	@Bean
	public MapperFacade mapperFacade(final BetrayalMapperFactory betrayalMapperFactory) throws Exception {
		return betrayalMapperFactory.getObject().getMapperFacade();
	}

	@Bean
	public MongoDbFactory mongoDbFactory() throws UnknownHostException {
		return new SimpleMongoDbFactory(new MongoClient(
				env.getProperty(BetrayalConst.MONGO_HOST_KEY, BetrayalConst.DOCKER_MONGO_HOST)), BetrayalConst.DB_NAME);
	}

	@Inject
	@Bean
	public MongoTemplate mongoTemplate(final MongoDbFactory mongoDbFactory) {
		return new MongoTemplate(mongoDbFactory);
	}
}