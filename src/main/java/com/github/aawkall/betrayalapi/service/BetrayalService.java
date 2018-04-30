package com.github.aawkall.betrayalapi.service;

import java.util.List;

import com.github.aawkall.betrayalapi.entity.db.Betrayal;
import com.github.aawkall.betrayalapi.entity.db.Player;
import com.github.aawkall.betrayalapi.entity.db.Player.Character;
import com.github.aawkall.betrayalapi.exception.BetrayalException;
import com.github.aawkall.betrayalapi.util.BetrayalConst.Stat;

public interface BetrayalService {

	// Betrayal game methods
	Betrayal createBetrayalGame(String betrayalId) throws BetrayalException;
	Betrayal getBetrayalGame(String betrayalId) throws BetrayalException;
	int getHauntNumber(String betrayalId) throws BetrayalException;
	Betrayal setHauntNumber(String betrayalId, int hauntNumber) throws BetrayalException;
	Player addPlayer(String betrayalId, Character character, String name) throws BetrayalException;

	// Player info methods
	Player getPlayer(String betrayalId, Character character) throws BetrayalException;
	Player getPlayer(String betrayalId, String name) throws BetrayalException;
	List<Player> getAllPlayers(String betrayalId) throws BetrayalException;
	List<Player> getAllTraitors(String betrayalId) throws BetrayalException;
	List<Player> getAllHeroes(String betrayalId) throws BetrayalException;
	List<Player> getAllAlive(String betrayalId) throws BetrayalException;
	List<Player> getAllDead(String betrayalId) throws BetrayalException;

	// Update player tags (alive / dead, traitor / hero)
	Player markAsDead(String betrayalId, String name) throws BetrayalException;
	Player markAsDead(String betrayalId, Character character) throws BetrayalException;
	Player markAsAlive(String betrayalId, String name) throws BetrayalException;
	Player markAsAlive(String betrayalId, Character character) throws BetrayalException;
	Player markAsTraitor(String betrayalId, String name) throws BetrayalException;
	Player markAsTraitor(String betrayalId, Character character) throws BetrayalException;
	Player markAsHero(String betrayalId, String name) throws BetrayalException;
	Player markAsHero(String betrayalId, Character character) throws BetrayalException;

	// Update player stats
	Player resetStatIndex(String betrayalId, String name, Stat stat) throws BetrayalException;
	Player resetStatIndex(String betrayalId, Character character, Stat stat) throws BetrayalException;
	Player incrementStatIndex(String betrayalId, String name, Stat stat) throws BetrayalException;
	Player incrementStatIndex(String betrayalId, Character character, Stat stat) throws BetrayalException;
	Player decrementStatIndex(String betrayalId, String name, Stat stat) throws BetrayalException;
	Player decrementStatIndex(String betrayalId, Character character, Stat stat) throws BetrayalException;

	// Delete player / game
	void deletePlayer(String betrayalId, String name) throws BetrayalException;
	void deletePlayer(String betrayalId, Character character) throws BetrayalException;
	void deleteBetrayalGame(String betrayalId) throws BetrayalException;
}