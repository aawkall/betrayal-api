package com.github.aawkall.betrayalapi.mapper;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.github.aawkall.betrayalapi.entity.db.Player;
import com.github.aawkall.betrayalapi.entity.db.Player.Character;
import com.github.aawkall.betrayalapi.entity.response.PlayerResponse;
import com.github.aawkall.betrayalapi.exception.BetrayalException;
import com.github.aawkall.betrayalapi.service.CharacterDataService;
import com.github.aawkall.betrayalapi.util.BetrayalConst.Stat;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;

@Component
public class PlayerMapper extends CustomMapper<Player, PlayerResponse> {
	private static final Logger log = LogManager.getLogger(PlayerMapper.class);

	private CharacterDataService characterDataService;

	@Inject
	public PlayerMapper(final CharacterDataService characterDataService) {
		this.characterDataService = characterDataService;
	}

	@Override
	public void mapAtoB(Player player, PlayerResponse playerResponse, MappingContext context) {
		Character character = player.getCharacter();

		playerResponse.setBetrayalId(player.getBetrayalId());
		playerResponse.setCharacter(character);
		playerResponse.setName(player.getName());
		playerResponse.setIsTraitor(player.getIsTraitor());
		playerResponse.setIsAlive(player.getIsAlive());
		try {
			playerResponse.setSpeed(characterDataService.getStatValue(Stat.SPEED, player.getSpeedIndex(), character));
			playerResponse.setMight(characterDataService.getStatValue(Stat.MIGHT, player.getMightIndex(), character));
			playerResponse.setSanity(characterDataService.getStatValue(Stat.SANITY, player.getSanityIndex(), character));
			playerResponse.setKnowledge(characterDataService.getStatValue(Stat.KNOWLEDGE, player.getKnowledgeIndex(), character));
		} catch (BetrayalException e) {
			// Unfortunately can't throw up exceptions to be processed by Jersey exception mapper
			// But, all Player objects being mapped were pulled straight from the DB, so we can ensure that
			// the stat indices are within range, as they would have been caught when originally changing the stat index.
			log.error(String.format("Exception thrown while accessing stat values in PlayerMapper: %s: %s",
					e.getHttpCode(), e.getMessage()));
		}
	}
}