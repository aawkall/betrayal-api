package com.github.aawkall.betrayalapi.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.github.aawkall.betrayalapi.entity.db.Betrayal;

public interface BetrayalRepository extends MongoRepository<Betrayal, String> {

	@Query("{ 'betrayalId' : ?0 }")
	List<Betrayal> findByBetrayalId(String betrayalId);

	@Query(value = "{'betrayalId': ?0 }", delete = true)
	List<Betrayal> deleteByBetrayalId(String betrayalId);
}