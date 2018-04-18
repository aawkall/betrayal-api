package com.github.aawkall.betrayalapi.service;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.github.aawkall.betrayalapi.entity.Betrayal;
import com.github.aawkall.betrayalapi.entity.Player;
import com.github.aawkall.betrayalapi.entity.Player.Character;
import com.github.aawkall.betrayalapi.exception.BetrayalException;
import com.github.aawkall.betrayalapi.repository.BetrayalRepository;
import com.github.aawkall.betrayalapi.repository.PlayerRepository;
import com.github.aawkall.betrayalapi.util.BetrayalConst;
import com.github.aawkall.betrayalapi.util.BetrayalConst.Stat;

@Service
public class BetrayalServiceImpl implements BetrayalService {
	private static final Logger log = LogManager.getLogger(BetrayalServiceImpl.class);

	private final BetrayalRepository betrayalRepository;
	private final PlayerRepository playerRepository;
	private final CharacterDataService characterDataService;

	@Inject
	public BetrayalServiceImpl(final BetrayalRepository betrayalRepository, final PlayerRepository playerRepository,
			final CharacterDataService characterDataService) {
		this.betrayalRepository = betrayalRepository;
		this.playerRepository = playerRepository;
		this.characterDataService = characterDataService;
	}

	@Override
	public Betrayal createBetrayalGame(final String channelId) throws BetrayalException {
		// Check if betrayal game with given channelId already exists
		List<Betrayal> games = betrayalRepository.findByChannelId(channelId);
		if (games.size() != 0) {
			throw new BetrayalException(String.format("Cannot create Betrayal game; game with channelId: %s already exists", channelId),
					Response.Status.CONFLICT);
		}

		Date timeCreated = new Date();
		Betrayal betrayal = new Betrayal(channelId, timeCreated);

		log.info("Creating Betrayal game, channelId: {}, timeCreated: {}", channelId, timeCreated);
		betrayal = betrayalRepository.save(betrayal);
		return betrayal;
	}

	@Override
	public Betrayal getBetrayalGame(final String channelId) throws BetrayalException {
		List<Betrayal> games = betrayalRepository.findByChannelId(channelId);
		if (games.size() == 0) {
			throw new BetrayalException(String.format("Betrayal game with channelId: %s does not exist", channelId),
					Response.Status.NOT_FOUND);
		}
		return games.get(0);
	}

	@Override
	public int getHauntNumber(final String channelId) throws BetrayalException {
		return getBetrayalGame(channelId).getHauntNumber();
	}

	@Override
	public Betrayal setHauntNumber(final String channelId, final int hauntNumber) throws BetrayalException {
		// Make sure haunt number is within range
		if (hauntNumber < 1 || hauntNumber > BetrayalConst.MAX_HAUNT_NUMBER) {
			throw new BetrayalException("Haunt number must be between 1 and 100", Response.Status.BAD_REQUEST);
		}

		Betrayal betrayal = getBetrayalGame(channelId);
		betrayal.setHauntNumber(hauntNumber);
		betrayalRepository.save(betrayal);
		return betrayal;
	}

	@Override
	public Player addPlayer(final String channelId, final String name, final Character character) throws BetrayalException {
		// Ensure betrayal game with channelId exists (if DNE, exception will be thrown from this method)
		getBetrayalGame(channelId);

		// Ensure there is not a player with the given name or character
		List<Player> players = playerRepository.findByChannelIdAndName(channelId, name);
		if (players.size() != 0) {
			throw new BetrayalException(String.format("Cannot add player to Betrayal game with channelId: %s; player with name: %s already exists", channelId, name),
					Response.Status.CONFLICT);
		}
		players = playerRepository.findByChannelIdAndCharacter(channelId, character);
		if (players.size() != 0) {
			throw new BetrayalException(String.format("Cannot add player to Betrayal game with channelId: %s; player with character: %s already exists", channelId, character),
					Response.Status.CONFLICT);
		}

		// Ensure chosen character's flip side isn't already in the game
		Character flipSide = characterDataService.getFlipSide(character);
		players = playerRepository.findByChannelIdAndCharacter(channelId, flipSide);
		if (players.size() != 0) {
			throw new BetrayalException(String.format("Cannot add player to Betrayal game with channelId: %s; player with flip side character, %s, already exists", channelId, flipSide),
					Response.Status.CONFLICT);
		}

		Player player = new Player(channelId, name, character);
		player = initializeStatIndices(player);

		log.info("Adding new player to Betrayal game, channelId: {}, name: {}, character: {}", channelId, name, character.toString());
		player = playerRepository.save(player);
		return player;
	}

	@Override
	public Player getPlayer(final String channelId, final String name) throws BetrayalException {
		List<Player> players = playerRepository.findByChannelIdAndName(channelId, name);
		if (players.size() == 0) {
			throw new BetrayalException(String.format("Player with channelId: %s and name: %s does not exist", channelId, name),
					Response.Status.NOT_FOUND);
		}
		return players.get(0);
	}

	@Override
	public Player getPlayer(final String channelId, final Character character) throws BetrayalException {
		List<Player> players = playerRepository.findByChannelIdAndCharacter(channelId, character);
		if (players.size() == 0) {
			throw new BetrayalException(String.format("Player with channelId: %s and character: %s does not exist", channelId, character),
					Response.Status.NOT_FOUND);
		}
		return players.get(0);
	}

	@Override
	public List<Player> getAllPlayers(final String channelId) {
		return playerRepository.findByChannelId(channelId);
	}

	@Override
	public List<Player> getAllTraitors(final String channelId) {
		return playerRepository.findByChannelIdAndIsTraitor(channelId, true);
	}

	@Override
	public List<Player> getAllHeroes(final String channelId) {
		return playerRepository.findByChannelIdAndIsTraitor(channelId, false);
	}

	@Override
	public List<Player> getAllAlive(final String channelId) {
		return playerRepository.findByChannelIdAndIsAlive(channelId, true);
	}

	@Override
	public List<Player> getAllDead(final String channelId) {
		return playerRepository.findByChannelIdAndIsAlive(channelId, false);
	}

	@Override
	public Player markAsDead(String channelId, String name) throws BetrayalException {
		Player currentPlayer = getPlayer(channelId, name);
		currentPlayer.setIsAlive(false);
		playerRepository.save(currentPlayer);
		return currentPlayer;
	}

	@Override
	public Player markAsDead(String channelId, Character character) throws BetrayalException {
		Player currentPlayer = getPlayer(channelId, character);
		currentPlayer.setIsAlive(false);
		playerRepository.save(currentPlayer);
		return currentPlayer;
	}

	@Override
	public Player markAsAlive(String channelId, String name) throws BetrayalException {
		Player currentPlayer = getPlayer(channelId, name);
		currentPlayer.setIsAlive(true);
		playerRepository.save(currentPlayer);
		return currentPlayer;
	}

	@Override
	public Player markAsAlive(String channelId, Character character) throws BetrayalException {
		Player currentPlayer = getPlayer(channelId, character);
		currentPlayer.setIsAlive(true);
		playerRepository.save(currentPlayer);
		return currentPlayer;
	}

	@Override
	public Player markAsTraitor(String channelId, String name) throws BetrayalException {
		Player currentPlayer = getPlayer(channelId, name);
		currentPlayer.setIsTraitor(true);
		playerRepository.save(currentPlayer);
		return currentPlayer;
	}

	@Override
	public Player markAsTraitor(String channelId, Character character) throws BetrayalException {
		Player currentPlayer = getPlayer(channelId, character);
		currentPlayer.setIsTraitor(true);
		playerRepository.save(currentPlayer);
		return currentPlayer;
	}

	@Override
	public Player markAsHero(String channelId, String name) throws BetrayalException {
		Player currentPlayer = getPlayer(channelId, name);
		currentPlayer.setIsTraitor(false);
		playerRepository.save(currentPlayer);
		return currentPlayer;
	}

	@Override
	public Player markAsHero(String channelId, Character character) throws BetrayalException {
		Player currentPlayer = getPlayer(channelId, character);
		currentPlayer.setIsTraitor(false);
		playerRepository.save(currentPlayer);
		return currentPlayer;
	}

	@Override
	public Player resetStatIndex(String channelId, String name, Stat stat) throws BetrayalException {
		Player currentPlayer = getPlayer(channelId, name);
		currentPlayer.setStatIndex(stat, characterDataService.getStatBaseIndex(stat, currentPlayer.getCharacter()));
		playerRepository.save(currentPlayer);
		return currentPlayer;
	}

	@Override
	public Player resetStatIndex(String channelId, Character character, Stat stat) throws BetrayalException {
		Player currentPlayer = getPlayer(channelId, character);
		currentPlayer.setStatIndex(stat, characterDataService.getStatBaseIndex(stat, character));
		playerRepository.save(currentPlayer);
		return currentPlayer;
	}

	@Override
	public Player incrementStatIndex(String channelId, String name, Stat stat) throws BetrayalException {
		Player currentPlayer = getPlayer(channelId, name);
		int currentStatIndex = currentPlayer.getStatIndex(stat);

		// Can't increment stat beyond max index
		if (currentStatIndex == BetrayalConst.MAX_STAT_INDEX) {
			throw new BetrayalException(String.format("Cannot increment %s beyond max index for channelId: %s, player: %s", stat, channelId, name),
					Response.Status.BAD_REQUEST);
		}

		// Increment stat index
		currentStatIndex++;
		currentPlayer.setStatIndex(stat, currentStatIndex);
		playerRepository.save(currentPlayer);
		return currentPlayer;
	}

	@Override
	public Player incrementStatIndex(String channelId, Character character, Stat stat) throws BetrayalException {
		Player currentPlayer = getPlayer(channelId, character);
		int currentStatIndex = currentPlayer.getStatIndex(stat);

		// Can't increment stat beyond max index
		if (currentStatIndex == BetrayalConst.MAX_STAT_INDEX) {
			throw new BetrayalException(String.format("Cannot increment %s beyond max index for channelId: %s, player: %s", stat, channelId, character),
					Response.Status.BAD_REQUEST);
		}

		// Increment stat index
		currentStatIndex++;
		currentPlayer.setStatIndex(stat, currentStatIndex);
		playerRepository.save(currentPlayer);
		return currentPlayer;
	}

	@Override
	public Player decrementStatIndex(String channelId, String name, Stat stat) throws BetrayalException {
		Player currentPlayer = getPlayer(channelId, name);
		int currentStatIndex = currentPlayer.getStatIndex(stat);

		// Can't decrement stat beyond 0
		if (currentStatIndex == 0) {
			throw new BetrayalException(String.format("Cannot decrement %s beyond max index for channelId: %s, player: %s", stat, channelId, name),
					Response.Status.BAD_REQUEST);
		}

		// Decrement stat index
		currentStatIndex--;
		currentPlayer.setStatIndex(stat, currentStatIndex);

		// If stat index is now 0, set player as dead
		if (currentStatIndex == 0) {
			currentPlayer.setIsAlive(false);
		}

		playerRepository.save(currentPlayer);
		return currentPlayer;
	}

	@Override
	public Player decrementStatIndex(String channelId, Character character, Stat stat) throws BetrayalException {
		Player currentPlayer = getPlayer(channelId, character);
		int currentStatIndex = currentPlayer.getStatIndex(stat);

		// Can't decrement stat beyond 0
		if (currentStatIndex == 0) {
			throw new BetrayalException(String.format("Cannot decrement %s beyond max index for channelId: %s, player: %s", stat, channelId, character),
					Response.Status.BAD_REQUEST);
		}

		// Decrement stat index
		currentStatIndex--;
		currentPlayer.setStatIndex(stat, currentStatIndex);

		// If stat index is now 0, set player as dead
		if (currentStatIndex == 0) {
			currentPlayer.setIsAlive(false);
		}

		playerRepository.save(currentPlayer);
		return currentPlayer;
	}

	@Override
	public void deletePlayer(final String channelId, final String name) throws BetrayalException {
		List<Player> deletedPlayers = playerRepository.deleteByChannelIdAndName(channelId, name);
		if (deletedPlayers.size() == 0) {
			throw new BetrayalException(String.format("Cannot delete player; player with channelId: %s and name: %s does not exist", channelId, name),
					Response.Status.NOT_FOUND);
		}
	}

	@Override
	public void deletePlayer(final String channelId, final Character character) throws BetrayalException {
		List<Player> deletedPlayers = playerRepository.deleteByChannelIdAndCharacter(channelId, character);
		if (deletedPlayers.size() == 0) {
			throw new BetrayalException(String.format("Cannot delete player; player with channelId: %s and character: %s does not exist", channelId, character),
					Response.Status.NOT_FOUND);
		}
	}

	@Override
	public void deleteBetrayalGame(final String channelId) throws BetrayalException {

		// Delete betrayal game
		List<Betrayal> deletedGames = betrayalRepository.deleteByChannelId(channelId);
		if (deletedGames.size() == 0) {
			throw new BetrayalException(String.format("Cannot delete betrayal game; game with channelId: %s does not exist", channelId),
					Response.Status.NOT_FOUND);
		}

		// Cascade delete all players for game
		playerRepository.deleteAllByChannelId(channelId);
	}

	// Initialize all stat indices on the player, given the chosen character
	private Player initializeStatIndices(final Player player) throws BetrayalException {
		Character playerCharacter = player.getCharacter();
		player.setSpeedIndex(characterDataService.getStatBaseIndex(Stat.SPEED, playerCharacter));
		player.setMightIndex(characterDataService.getStatBaseIndex(Stat.MIGHT, playerCharacter));
		player.setSanityIndex(characterDataService.getStatBaseIndex(Stat.SANITY, playerCharacter));
		player.setKnowledgeIndex(characterDataService.getStatBaseIndex(Stat.KNOWLEDGE, playerCharacter));
		return player;
	}
}