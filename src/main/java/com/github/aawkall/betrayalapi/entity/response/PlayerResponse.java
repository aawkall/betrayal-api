package com.github.aawkall.betrayalapi.entity.response;

import java.io.IOException;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.aawkall.betrayalapi.entity.db.Player.Character;

@JsonSerialize(using = PlayerResponse.PlayerSerializer.class)
public final class PlayerResponse {

	private String betrayalId;
	private Character character;
	private String name;
	private boolean isTraitor;
	private boolean isAlive;
	private int speed;
	private int might;
	private int sanity;
	private int knowledge;

	private boolean includeBetrayalId;
	private boolean includeCharacter;
	private boolean includeName;
	private boolean includeIsTraitor;
	private boolean includeIsAlive;
	private boolean includeSpeed;
	private boolean includeMight;
	private boolean includeSanity;
	private boolean includeKnowledge;

	public PlayerResponse() {
		// All fields are included by default
		includeBetrayalId = true;
		includeCharacter = true;
		includeName = true;
		includeIsTraitor = true;
		includeIsAlive = true;
		includeSpeed = true;
		includeMight = true;
		includeSanity = true;
		includeKnowledge = true;
	}

	public String getBetrayalId() {
		return betrayalId;
	}

	public void setBetrayalId(String betrayalId) {
		this.betrayalId = betrayalId;
	}

	public Character getCharacter() {
		return character;
	}

	public void setCharacter(Character character) {
		this.character = character;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean getIsTraitor() {
		return isTraitor;
	}

	public void setIsTraitor(boolean isTraitor) {
		this.isTraitor = isTraitor;
	}

	public boolean getIsAlive() {
		return isAlive;
	}

	public void setIsAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getMight() {
		return might;
	}

	public void setMight(int might) {
		this.might = might;
	}

	public int getSanity() {
		return sanity;
	}

	public void setSanity(int sanity) {
		this.sanity = sanity;
	}

	public int getKnowledge() {
		return knowledge;
	}

	public void setKnowledge(int knowledge) {
		this.knowledge = knowledge;
	}

	public void initFieldFiltering() {
		/* When filtering, betrayalId, character, and name are included by default, and other fields are not included
		   Fields can be included by calling the individual "include" methods. The include variables
		   are used when the object is serialized */
		includeBetrayalId = true;
		includeCharacter = true;
		includeName = true;
		includeIsTraitor = false;
		includeIsAlive = false;
		includeSpeed = false;
		includeMight = false;
		includeSanity = false;
		includeKnowledge = false;
	}

	public void setIncludeBetrayalId(boolean includeBetrayalId) {
		this.includeBetrayalId = includeBetrayalId;
	}

	public boolean isBetrayalIdIncluded() {
		return includeBetrayalId;
	}

	public void setIncludeCharacter(boolean includeCharacter) {
		this.includeCharacter = includeCharacter;
	}

	public boolean isCharacterIncluded() {
		return includeCharacter;
	}

	public void setIncludeName(boolean includeName) {
		this.includeName = includeName;
	}

	public boolean isNameIncluded() {
		return includeName;
	}

	public void setIncludeIsTraitor(boolean includeIsTraitor) {
		this.includeIsTraitor = includeIsTraitor;
	}

	public boolean isIsTraitorIncluded() {
		return includeIsTraitor;
	}

	public void setIncludeIsAlive(boolean includeIsAlive) {
		this.includeIsAlive = includeIsAlive;
	}

	public boolean isIsAliveIncluded() {
		return includeIsAlive;
	}

	public void setIncludeSpeed(boolean includeSpeed) {
		this.includeSpeed = includeSpeed;
	}

	public boolean isSpeedIncluded() {
		return includeSpeed;
	}

	public void setIncludeMight(boolean includeMight) {
		this.includeMight = includeMight;
	}

	public boolean isMightIncluded() {
		return includeMight;
	}

	public void setIncludeSanity(boolean includeSanity) {
		this.includeSanity = includeSanity;
	}

	public boolean isSanityIncluded() {
		return includeSanity;
	}

	public void setIncludeKnowledge(boolean includeKnowledge) {
		this.includeKnowledge = includeKnowledge;
	}

	public boolean isKnowledgeIncluded() {
		return includeKnowledge;
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
		return String.format("PlayerResponse[id=%s, betrayalId=%s, character=%s, name=%s, isTraitor=%s, isAlive=%s, speed=%s, might=%s, sanity=%s, knowledge=%s]",
				betrayalId, character, name, isTraitor, isAlive, speed, might, sanity, knowledge);
	}

	public static class PlayerSerializer extends StdSerializer<PlayerResponse> {
		private static final long serialVersionUID = 1L;

		@SuppressWarnings("unused")
		public PlayerSerializer() {
			this(null);
		}

		public PlayerSerializer(Class<PlayerResponse> t) {
			super(t);
		}

		@Override
		public void serialize(PlayerResponse playerResponse, JsonGenerator jgen, SerializerProvider sp)
				throws IOException, JsonProcessingException {
			jgen.writeStartObject();

			if (playerResponse.isBetrayalIdIncluded()) {
				jgen.writeStringField("betrayalId", playerResponse.getBetrayalId());
			}
			if (playerResponse.isCharacterIncluded()) {
				jgen.writeStringField("character", playerResponse.getCharacter().toString());
			}
			if (playerResponse.isNameIncluded()) {
				jgen.writeStringField("name", playerResponse.getName());
			}
			if (playerResponse.isIsTraitorIncluded()) {
				jgen.writeBooleanField("isTraitor", playerResponse.getIsTraitor());
			}
			if (playerResponse.isIsAliveIncluded()) {
				jgen.writeBooleanField("isAlive", playerResponse.getIsAlive());
			}
			if (playerResponse.isSpeedIncluded()) {
				jgen.writeNumberField("speed", playerResponse.getSpeed());
			}
			if (playerResponse.isMightIncluded()) {
				jgen.writeNumberField("might", playerResponse.getMight());
			}
			if (playerResponse.isSanityIncluded()) {
				jgen.writeNumberField("sanity", playerResponse.getSanity());
			}
			if (playerResponse.isKnowledgeIncluded()) {
				jgen.writeNumberField("knowledge", playerResponse.getKnowledge());
			}

			jgen.writeEndObject();
		}
	}
}