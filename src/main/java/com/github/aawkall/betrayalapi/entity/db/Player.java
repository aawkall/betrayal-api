package com.github.aawkall.betrayalapi.entity.db;

import java.util.EnumSet;
import java.util.Objects;
import java.util.StringTokenizer;

import javax.ws.rs.core.Response;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.github.aawkall.betrayalapi.exception.BetrayalException;
import com.github.aawkall.betrayalapi.util.BetrayalConst;
import com.github.aawkall.betrayalapi.util.BetrayalConst.Stat;

@Document(collection = BetrayalConst.PLAYER_COLLECTION)
@CompoundIndexes({
		@CompoundIndex(name = "betrayalId_character_index", def = "{'betrayalId' : 1, 'character': 1}"),
		@CompoundIndex(name = "betrayalId_name_index", def = "{'betrayalId' : 1, 'name': 1}"),
		@CompoundIndex(name = "betrayalId_isTraitor_index", def = "{'betrayalId' : 1, 'isTraitor': 1}"),
		@CompoundIndex(name = "betrayalId_isAlive_index", def = "{'betrayalId' : 1, 'isAlive': 1}")
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

		public static Character fromString(String text) throws BetrayalException {
			// Process each token from the input text separately, to see if any tokens match a character name
			StringTokenizer st = new StringTokenizer(text, "_- \t\n\r\f");
			EnumSet<Character> characters = EnumSet.allOf(Player.Character.class);
			Character character = null;
			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				for (Character c : characters) {
					if (c.characterName.toLowerCase().replace(" ", "").contains(token.toLowerCase())) {
						// If we haven't found a matching character yet, store this one
						if (character == null) {
							character = c;
						} else if (character != c) {
							// We found another different character that matches - throw an exception
							throw new BetrayalException(String.format("More than one character was found matching String: %s", text),
									Response.Status.BAD_REQUEST);
						}
					}
				}
			}
			return character;
		}

		@Override
		public String toString() {
			return characterName;
		}
	}

	@Id
	private String _id;

	@Indexed
	private String betrayalId;
	private Character character;
	private String name;
	private boolean isTraitor;
	private boolean isAlive;
	private int speedIndex;
	private int mightIndex;
	private int sanityIndex;
	private int knowledgeIndex;

	public Player(String betrayalId, Character character, String name) {
		this.betrayalId = betrayalId;
		this.character = character;
		this.name = name;
		this.isTraitor = false;
		this.isAlive = true;
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
		switch (stat) {
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
		switch (stat) {
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

	@Override
	public boolean equals(Object object) {
		if (object == this) {
			return true;
		}
		if (!(object instanceof Player)) {
			return false;
		}

		Player toCompare = (Player) object;
		return Objects.equals(betrayalId, toCompare.getBetrayalId())
				&& character == toCompare.getCharacter()
				&& Objects.equals(name, toCompare.getName())
				&& speedIndex == toCompare.getSpeedIndex()
				&& mightIndex == toCompare.getMightIndex()
				&& sanityIndex == toCompare.getSanityIndex()
				&& knowledgeIndex == toCompare.getKnowledgeIndex()
				&& isTraitor == toCompare.getIsTraitor()
				&& isAlive == toCompare.getIsAlive();
	}

	@Override
	public String toString() {
		return String.format("Player[id=%s, betrayalId=%s, character=%s, name=%s, speedIndex=%s, mightIndex=%s, sanityIndex=%s, knowledgeIndex=%s, isTraitor=%s, isAlive=%s]",
				_id, betrayalId, character, name, speedIndex, mightIndex, sanityIndex, knowledgeIndex, isTraitor, isAlive);
	}
}