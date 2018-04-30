package com.github.aawkall.betrayalapi.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.github.aawkall.betrayalapi.entity.db.Player;
import com.github.aawkall.betrayalapi.entity.db.Player.Character;

public interface PlayerRepository extends MongoRepository<Player, String> {

	@Query("{'betrayalId': ?0 }")
	List<Player> findByBetrayalId(String betrayalId);

	@Query("{'betrayalId': ?0, 'name': ?1 }")
	List<Player> findByBetrayalIdAndName(String betrayalId, String name);

	@Query("{'betrayalId': ?0, 'character': ?1 }")
	List<Player> findByBetrayalIdAndCharacter(String betrayalId, Character character);

	@Query("{'betrayalId': ?0, 'isTraitor': ?1 }")
	List<Player> findByBetrayalIdAndIsTraitor(String betrayalId, boolean isTraitor);

	@Query("{'betrayalId': ?0, 'isAlive': ?1 }")
	List<Player> findByBetrayalIdAndIsAlive(String betrayalId, boolean isAlive);

	@Query(value = "{'betrayalId': ?0 }", delete = true)
	List<Player> deleteAllByBetrayalId(String betrayalId);

	@Query(value = "{'betrayalId': ?0, 'name': ?1 }", delete = true)
	List<Player> deleteByBetrayalIdAndName(String betrayalId, String name);

	@Query(value = "{'betrayalId': ?0, 'character': ?1 }", delete = true)
	List<Player> deleteByBetrayalIdAndCharacter(String betrayalId, Character character);
}