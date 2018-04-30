package com.github.aawkall.betrayalapi.service;

import com.github.aawkall.betrayalapi.entity.db.Player.Character;
import com.github.aawkall.betrayalapi.exception.BetrayalException;
import com.github.aawkall.betrayalapi.util.BetrayalConst.Stat;

public interface CharacterDataService {

	int getStatBaseIndex(Stat stat, Character character) throws BetrayalException;
	int getStatValue(Stat stat, int index, Character character) throws BetrayalException;
	String getColor(Character character) throws BetrayalException;
	Character getFlipSide(Character character) throws BetrayalException;
}