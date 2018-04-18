package com.github.aawkall.betrayalapi.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.github.aawkall.betrayalapi.entity.Player;
import com.github.aawkall.betrayalapi.entity.Player.Character;

public interface PlayerRepository extends MongoRepository<Player, String> {

	@Query("{'channelId': ?0 }")
	List<Player> findByChannelId(String channelId);

	@Query("{'channelId': ?0, 'name': ?1 }")
	List<Player> findByChannelIdAndName(String channelId, String name);

	@Query("{'channelId': ?0, 'character': ?1 }")
	List<Player> findByChannelIdAndCharacter(String channelId, Character character);

	@Query("{'channelId': ?0, 'isTraitor': ?1 }")
	List<Player> findByChannelIdAndIsTraitor(String channelId, boolean isTraitor);

	@Query("{'channelId': ?0, 'isAlive': ?1 }")
	List<Player> findByChannelIdAndIsAlive(String channelId, boolean isAlive);

	@Query(value ="{'channelId': ?0 }", delete = true)
	List<Player> deleteAllByChannelId(String channelId);

	@Query(value ="{'channelId': ?0, 'name': ?1 }", delete = true)
	List<Player> deleteByChannelIdAndName(String channelId, String name);

	@Query(value ="{'channelId': ?0, 'character': ?1 }", delete = true)
	List<Player> deleteByChannelIdAndCharacter(String channelId, Character character);
}