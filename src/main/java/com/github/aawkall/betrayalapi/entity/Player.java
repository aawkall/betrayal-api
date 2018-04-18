package com.github.aawkall.betrayalapi.entity;

import java.util.EnumSet;
import java.util.StringTokenizer;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.github.aawkall.betrayalapi.util.BetrayalConst.Stat;

@Document(collection = "players")
@CompoundIndexes({
	@CompoundIndex(name = "channelId_name_index", def = "{'channelId' : 1, 'name': 1}"),
	@CompoundIndex(name = "channelId_character_index", def = "{'channelId' : 1, 'character': 1}"),
	@CompoundIndex(name = "channelId_isTraitor_index", def = "{'channelId' : 1, 'isTraitor': 1}"),
	@CompoundIndex(name = "channelId_isAlive_index", def = "{'channelId' : 1, 'isAlive': 1}")
})
public final class Player {

	public enum Character {
		BRANDON_JASPERS("Brandon Jaspers"),
		DARRIN_WILLIAMS("Darrin \"Flash\" Williams"),
		FATHER_RHINEHARDT("Father Rhinehardt"),
		JENNY_LECLERC("Jenny LeClerc"),
		HEATHER_GRANVILLE("Heather Granville"),
		MADAME_ZOSTRA("Madame Zostra"),
		MISSY_DUBOURDE("Missy Dubourde"),
		OX_BELLOWS("Ox Bellows"),
		PETER_AKIMOTO("Peter Akimoto"),
		PROFESSOR_LONGFELLOW("Professor Longfellow"),
		VIVIAN_LOPEZ("Vivian Lopez"),
		ZOE_INGSTROM("Zoe Ingstrom");

		private String characterName;

		Character(String characterName) {
			this.characterName = characterName;
		}

		public static Character fromString(String text) {
			// Process each token from the input text separately, to see if any tokens match a character name
			StringTokenizer st = new StringTokenizer(text);
			EnumSet<Character> characters = EnumSet.allOf(Player.Character.class);
			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				for (Character c : characters) {
					if (c.characterName.toLowerCase().contains(token.toLowerCase())) {
						return c;
					}
				}
			}
			return null;
		}

		@Override
		public String toString() {
			return characterName;
		}
	}

	@Id
	private String _id;

	@Indexed
	private String channelId;
	private String name;
	private Character character;
	private int speedIndex;
	private int mightIndex;
	private int sanityIndex;
	private int knowledgeIndex;
	private boolean isTraitor;
	private boolean isAlive;

	public Player(String channelId, String name, Character character) {
		this.channelId = channelId;
		this.name = name;
		this.character = character;
		this.isTraitor = false;
		this.isAlive = true;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Character getCharacter() {
		return character;
	}

	public void setCharacter(Character character) {
		this.character = character;
	}

	public int getSpeedIndex() {
		return speedIndex;
	}

	public void setSpeedIndex(int speedIndex) {
		this.speedIndex = speedIndex;
	}

	public int getMightIndex() {
		return mightIndex;
	}

	public void setMightIndex(int mightIndex) {
		this.mightIndex = mightIndex;
	}

	public int getSanityIndex() {
		return sanityIndex;
	}

	public void setSanityIndex(int sanityIndex) {
		this.sanityIndex = sanityIndex;
	}

	public int getKnowledgeIndex() {
		return knowledgeIndex;
	}

	public void setKnowledgeIndex(int knowledgeIndex) {
		this.knowledgeIndex = knowledgeIndex;
	}

	public void setStatIndex(Stat stat, int index) {
		switch(stat) {
			case SPEED:
				this.setSpeedIndex(index);
				break;
			case MIGHT:
				this.setMightIndex(index);
				break;
			case SANITY:
				this.setSanityIndex(index);
				break;
			case KNOWLEDGE:
				this.setKnowledgeIndex(index);
				break;
		}
	}

	public int getStatIndex(Stat stat) {
		int index = 0;
		switch(stat) {
			case SPEED:
				index = this.getSpeedIndex();
				break;
			case MIGHT:
				index = this.getMightIndex();
				break;
			case SANITY:
				index = this.getSanityIndex();
				break;
			case KNOWLEDGE:
				index = this.getKnowledgeIndex();
				break;
		}
		return index;
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

	@Override
	public String toString() {
		return String.format(
				"Player[id=%s, channelId=%s, name=%s, character=%s, speedIndex=%s, mightIndex=%s, sanityIndex=%s, knowledgeIndex=%s, isTraitor=%s, isAlive=%s]",
				_id, channelId, name, character, speedIndex, mightIndex, sanityIndex, knowledgeIndex, isTraitor, isAlive);
	}
}