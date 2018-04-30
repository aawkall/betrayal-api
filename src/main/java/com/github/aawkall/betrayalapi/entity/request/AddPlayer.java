package com.github.aawkall.betrayalapi.entity.request;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.github.aawkall.betrayalapi.entity.db.Player.Character;
import com.github.aawkall.betrayalapi.exception.BetrayalException;

public class AddPlayer {

	private Character character;
	private String name;

	public Character getCharacter() {
		return character;
	}

	public void setCharacter(String character) throws BetrayalException {
		this.character = Character.fromString(character);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object object) {
		return EqualsBuilder.reflectionEquals(object, this);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public String toString() {
		return String.format("AddPlayer[character=%s, name=%s]", character.toString(), name);
	}
}