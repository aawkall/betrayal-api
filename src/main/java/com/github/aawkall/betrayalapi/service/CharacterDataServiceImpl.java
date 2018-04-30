package com.github.aawkall.betrayalapi.service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.ws.rs.core.Response;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import com.github.aawkall.betrayalapi.entity.db.Player.Character;
import com.github.aawkall.betrayalapi.exception.BetrayalException;
import com.github.aawkall.betrayalapi.util.BetrayalConst;
import com.github.aawkall.betrayalapi.util.BetrayalConst.Stat;

@Service
public class CharacterDataServiceImpl implements CharacterDataService {
	private static final String DATAFILE = "characters.json";

	@Override
	public int getStatBaseIndex(final Stat stat, final Character character) throws BetrayalException {
		JSONObject allData = getAllCharacterData();
		JSONObject characterData = (JSONObject) allData.get(character.name().toLowerCase());
		return ((Long) characterData.get(String.format("%s_base_index", stat.name().toLowerCase()))).intValue();
	}

	@Override
	public int getStatValue(final Stat stat, final int index, final Character character) throws BetrayalException {
		// Make sure index is within range
		if (index < 0 || index > BetrayalConst.MAX_STAT_INDEX) {
			throw new BetrayalException(String.format("Index must be between 0 and %s", BetrayalConst.MAX_STAT_INDEX), Response.Status.INTERNAL_SERVER_ERROR);
		}
		JSONObject allData = getAllCharacterData();
		JSONObject characterData = (JSONObject) allData.get(character.name().toLowerCase());
		JSONArray statArray = (JSONArray) characterData.get(stat.name().toLowerCase());
		return ((Long) statArray.get(index)).intValue();
	}

	@Override
	public String getColor(final Character character) throws BetrayalException {
		JSONObject allData = getAllCharacterData();
		JSONObject characterData = (JSONObject) allData.get(character.name().toLowerCase());
		return (String) characterData.get("color");
	}

	@Override
	public Character getFlipSide(final Character character) throws BetrayalException {
		JSONObject allData = getAllCharacterData();
		JSONObject characterData = (JSONObject) allData.get(character.name().toLowerCase());
		return Character.fromString((String) characterData.get("flip_side"));
	}

	private JSONObject getAllCharacterData() throws BetrayalException {
		JSONObject allData = null;
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			File characterFile = new File(classLoader.getResource(DATAFILE).getFile());
			JSONParser parser = new JSONParser();
			Object object = parser.parse(new FileReader(characterFile));
			allData = (JSONObject) object;
		} catch (IOException | ParseException e) {
			throw new BetrayalException("Error reading characters.json", e, Response.Status.INTERNAL_SERVER_ERROR);
		}
		return allData;
	}
}