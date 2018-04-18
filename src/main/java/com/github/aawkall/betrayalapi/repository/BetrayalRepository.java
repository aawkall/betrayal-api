package com.github.aawkall.betrayalapi.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.github.aawkall.betrayalapi.entity.Betrayal;

public interface BetrayalRepository extends MongoRepository<Betrayal, String> {

	@Query("{ 'channelId' : ?0 }")
	List<Betrayal> findByChannelId(String channelId);

	@Query(value ="{'channelId': ?0 }", delete = true)
	List<Betrayal> deleteByChannelId(String channelId);
}