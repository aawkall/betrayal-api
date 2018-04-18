package com.github.aawkall.betrayalapi.service;

import java.util.List;

import com.github.aawkall.betrayalapi.entity.Betrayal;
import com.github.aawkall.betrayalapi.entity.Player;
import com.github.aawkall.betrayalapi.entity.Player.Character;
import com.github.aawkall.betrayalapi.exception.BetrayalException;
import com.github.aawkall.betrayalapi.util.BetrayalConst.Stat;

public interface BetrayalService {

	// Betrayal game methods
	Betrayal createBetrayalGame(String channelId) throws BetrayalException;
	Betrayal getBetrayalGame(String channelId) throws BetrayalException;
	int getHauntNumber(String channelId) throws BetrayalException;
	Betrayal setHauntNumber(String channelId, int hauntNumber) throws BetrayalException;
	Player addPlayer(String channelId, String name, Character character) throws BetrayalException;

	// Player info methods
	Player getPlayer(String channelId, String name) throws BetrayalException;
	Player getPlayer(String channelId, Character character) throws BetrayalException;
	List<Player> getAllPlayers(String channelId);
	List<Player> getAllTraitors(String channelId);
	List<Player> getAllHeroes(String channelId);
	List<Player> getAllAlive(String channelId);
	List<Player> getAllDead(String channelId);

	// Update player tags (alive / dead, traitor / hero)
	Player markAsDead(String channelId, String name) throws BetrayalException;
	Player markAsDead(String channelId, Character character) throws BetrayalException;
	Player markAsAlive(String channelId, String name) throws BetrayalException;
	Player markAsAlive(String channelId, Character character) throws BetrayalException;
	Player markAsTraitor(String channelId, String name) throws BetrayalException;
	Player markAsTraitor(String channelId, Character character) throws BetrayalException;
	Player markAsHero(String channelId, String name) throws BetrayalException;
	Player markAsHero(String channelId, Character character) throws BetrayalException;

	// Update player stats
	Player resetStatIndex(String channelId, String name, Stat stat) throws BetrayalException;
	Player resetStatIndex(String channelId, Character character, Stat stat) throws BetrayalException;
	Player incrementStatIndex(String channelId, String name, Stat stat) throws BetrayalException;
	Player incrementStatIndex(String channelId, Character character, Stat stat) throws BetrayalException;
	Player decrementStatIndex(String channelId, String name, Stat stat) throws BetrayalException;
	Player decrementStatIndex(String channelId, Character character, Stat stat) throws BetrayalException;

	// Delete player / game
	void deletePlayer(String channelId, String name) throws BetrayalException;
	void deletePlayer(String channelId, Character character) throws BetrayalException;
	void deleteBetrayalGame(String channelId) throws BetrayalException;
}