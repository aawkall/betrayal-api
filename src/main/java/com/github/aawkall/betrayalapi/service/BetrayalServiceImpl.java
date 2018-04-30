package com.github.aawkall.betrayalapi.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.github.aawkall.betrayalapi.entity.db.Betrayal;
import com.github.aawkall.betrayalapi.entity.db.Player;
import com.github.aawkall.betrayalapi.entity.db.Player.Character;
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
	public Betrayal createBetrayalGame(final String betrayalId) throws BetrayalException {
		// Check if betrayal game with given betrayalId already exists
		List<Betrayal> games = betrayalRepository.findByBetrayalId(betrayalId);
		if (!games.isEmpty()) {
			throw new BetrayalException(String.format("Cannot create Betrayal game; game with betrayalId: %s already exists", betrayalId),
					Response.Status.CONFLICT);
		}

		Date timeCreated = new Date();
		Betrayal betrayal = new Betrayal(betrayalId, timeCreated);
		betrayal = betrayalRepository.save(betrayal);

		log.debug("Created Betrayal game, betrayalId: {}, timeCreated: {}", betrayalId, timeCreated);
		return betrayal;
	}

	@Override
	public Betrayal getBetrayalGame(final String betrayalId) throws BetrayalException {
		List<Betrayal> games = betrayalRepository.findByBetrayalId(betrayalId);
		if (games.isEmpty()) {
			throw new BetrayalException(String.format("Betrayal game with betrayalId: %s does not exist", betrayalId),
					Response.Status.NOT_FOUND);
		}
		return games.get(0);
	}

	@Override
	public int getHauntNumber(final String betrayalId) throws BetrayalException {
		return getBetrayalGame(betrayalId).getHauntNumber();
	}

	@Override
	public Betrayal setHauntNumber(final String betrayalId, final int hauntNumber) throws BetrayalException {
		// Make sure haunt number is within range
		if (hauntNumber < 1 || hauntNumber > BetrayalConst.MAX_HAUNT_NUMBER) {
			throw new BetrayalException("Haunt number must be between 1 and 100", Response.Status.BAD_REQUEST);
		}

		Betrayal betrayal = getBetrayalGame(betrayalId);
		betrayal.setHauntNumber(hauntNumber);
		betrayalRepository.save(betrayal);

		log.debug("Haunt number set to {} for Betrayal game with betrayalId: {}", hauntNumber, betrayalId);
		return betrayal;
	}

	@Override
	public Player addPlayer(final String betrayalId, final Character character, final String name) throws BetrayalException {
		// Ensure betrayal game with betrayalId exists (if DNE, exception will be thrown from this method)
		getBetrayalGame(betrayalId);

		// Ensure there is not a player with the given name or character
		List<Player> players = playerRepository.findByBetrayalIdAndName(betrayalId, name);
		if (!players.isEmpty()) {
			throw new BetrayalException(String.format("Cannot add player to Betrayal game with betrayalId: %s; player with name: %s already exists", betrayalId, name),
					Response.Status.CONFLICT);
		}
		players = playerRepository.findByBetrayalIdAndCharacter(betrayalId, character);
		if (!players.isEmpty()) {
			throw new BetrayalException(String.format("Cannot add player to Betrayal game with betrayalId: %s; player with character: %s already exists", betrayalId, character),
					Response.Status.CONFLICT);
		}

		// Ensure chosen character's flip side isn't already in the game
		Character flipSide = characterDataService.getFlipSide(character);
		players = playerRepository.findByBetrayalIdAndCharacter(betrayalId, flipSide);
		if (!players.isEmpty()) {
			throw new BetrayalException(String.format("Cannot add player to Betrayal game with betrayalId: %s; player with flip side character, %s, already exists", betrayalId, flipSide),
					Response.Status.CONFLICT);
		}

		Player player = new Player(betrayalId, character, name);
		player = initializeStatIndices(player);
		player = playerRepository.save(player);

		log.debug("Added new player to Betrayal game, betrayalId: {}, name: {}, character: {}", betrayalId, name, character.toString());
		return player;
	}

	@Override
	public Player getPlayer(final String betrayalId, final String name) throws BetrayalException {
		List<Player> players = playerRepository.findByBetrayalIdAndName(betrayalId, name);
		if (players.isEmpty()) {
			throw new BetrayalException(String.format("Player with betrayalId: %s and name: %s does not exist", betrayalId, name),
					Response.Status.NOT_FOUND);
		}
		return players.get(0);
	}

	@Override
	public Player getPlayer(final String betrayalId, final Character character) throws BetrayalException {
		List<Player> players = playerRepository.findByBetrayalIdAndCharacter(betrayalId, character);
		if (players.isEmpty()) {
			throw new BetrayalException(String.format("Player with betrayalId: %s and character: %s does not exist", betrayalId, character),
					Response.Status.NOT_FOUND);
		}
		return players.get(0);
	}

	@Override
	public List<Player> getAllPlayers(final String betrayalId) throws BetrayalException {
		List<Player> allPlayers = playerRepository.findByBetrayalId(betrayalId);

		// If there are no players, see if the betrayal game actually exists
		if (allPlayers.isEmpty()) {
			// If the game doesn't exist, getBetrayalGame will throw an exception with NOT_FOUND
			getBetrayalGame(betrayalId);

			// If the game does exist, there just aren't players yet - so return an empty list
			allPlayers = new ArrayList<>();
		}

		return allPlayers;
	}

	@Override
	public List<Player> getAllTraitors(final String betrayalId) throws BetrayalException {
		List<Player> allTraitors = playerRepository.findByBetrayalIdAndIsTraitor(betrayalId, true);

		// If there are no traitors, see if the betrayal game actually exists
		if (allTraitors.isEmpty()) {
			// If the game doesn't exist, getBetrayalGame will throw an exception with NOT_FOUND
			getBetrayalGame(betrayalId);

			// If the game does exist, there just aren't players yet - so return an empty list
			allTraitors = new ArrayList<>();
		}

		return allTraitors;
	}

	@Override
	public List<Player> getAllHeroes(final String betrayalId) throws BetrayalException {
		List<Player> allHeroes = playerRepository.findByBetrayalIdAndIsTraitor(betrayalId, false);

		// If there are no heroes, see if the betrayal game actually exists
		if (allHeroes.isEmpty()) {
			// If the game doesn't exist, getBetrayalGame will throw an exception with NOT_FOUND
			getBetrayalGame(betrayalId);

			// If the game does exist, there just aren't players yet - so return an empty list
			allHeroes = new ArrayList<>();
		}

		return allHeroes;
	}

	@Override
	public List<Player> getAllAlive(final String betrayalId) throws BetrayalException {
		List<Player> allAlive = playerRepository.findByBetrayalIdAndIsAlive(betrayalId, true);

		// If there are no players alive, see if the betrayal game actually exists
		if (allAlive.isEmpty()) {
			// If the game doesn't exist, getBetrayalGame will throw an exception with NOT_FOUND
			getBetrayalGame(betrayalId);

			// If the game does exist, there just aren't players yet - so return an empty list
			allAlive = new ArrayList<>();
		}

		return allAlive;
	}

	@Override
	public List<Player> getAllDead(final String betrayalId) throws BetrayalException {
		List<Player> allDead = playerRepository.findByBetrayalIdAndIsAlive(betrayalId, false);

		// If there are no players dead, see if the betrayal game actually exists
		if (allDead.isEmpty()) {
			// If the game doesn't exist, getBetrayalGame will throw an exception with NOT_FOUND
			getBetrayalGame(betrayalId);

			// If the game does exist, there just aren't players yet - so return an empty list
			allDead = new ArrayList<>();
		}

		return allDead;
	}

	@Override
	public Player markAsDead(String betrayalId, String name) throws BetrayalException {
		Player currentPlayer = getPlayer(betrayalId, name);
		currentPlayer.setIsAlive(false);
		playerRepository.save(currentPlayer);

		log.debug("Marked player with betrayalId: {} and name: {} as dead", betrayalId, name);
		return currentPlayer;
	}

	@Override
	public Player markAsDead(String betrayalId, Character character) throws BetrayalException {
		Player currentPlayer = getPlayer(betrayalId, character);
		currentPlayer.setIsAlive(false);
		playerRepository.save(currentPlayer);

		log.debug("Marked player with betrayalId: {} and character: {} as dead", betrayalId, character.toString());
		return currentPlayer;
	}

	@Override
	public Player markAsAlive(String betrayalId, String name) throws BetrayalException {
		Player currentPlayer = getPlayer(betrayalId, name);
		currentPlayer.setIsAlive(true);
		playerRepository.save(currentPlayer);

		log.debug("Marked player with betrayalId: {} and name: {} as alive", betrayalId, name);
		return currentPlayer;
	}

	@Override
	public Player markAsAlive(String betrayalId, Character character) throws BetrayalException {
		Player currentPlayer = getPlayer(betrayalId, character);
		currentPlayer.setIsAlive(true);
		playerRepository.save(currentPlayer);

		log.debug("Marked player with betrayalId: {} and character: {} as alive", betrayalId, character.toString());
		return currentPlayer;
	}

	@Override
	public Player markAsTraitor(String betrayalId, String name) throws BetrayalException {
		Player currentPlayer = getPlayer(betrayalId, name);
		currentPlayer.setIsTraitor(true);
		playerRepository.save(currentPlayer);

		log.debug("Marked player with betrayalId: {} and name: {} as a traitor", betrayalId, name);
		return currentPlayer;
	}

	@Override
	public Player markAsTraitor(String betrayalId, Character character) throws BetrayalException {
		Player currentPlayer = getPlayer(betrayalId, character);
		currentPlayer.setIsTraitor(true);
		playerRepository.save(currentPlayer);

		log.debug("Marked player with betrayalId: {} and character: {} as a traitor", betrayalId, character.toString());
		return currentPlayer;
	}

	@Override
	public Player markAsHero(String betrayalId, String name) throws BetrayalException {
		Player currentPlayer = getPlayer(betrayalId, name);
		currentPlayer.setIsTraitor(false);
		playerRepository.save(currentPlayer);

		log.debug("Marked player with betrayalId: {} and name: {} as a hero", betrayalId, name);
		return currentPlayer;
	}

	@Override
	public Player markAsHero(String betrayalId, Character character) throws BetrayalException {
		Player currentPlayer = getPlayer(betrayalId, character);
		currentPlayer.setIsTraitor(false);
		playerRepository.save(currentPlayer);

		log.debug("Marked player with betrayalId: {} and character: {} as a hero", betrayalId, character.toString());
		return currentPlayer;
	}

	@Override
	public Player resetStatIndex(String betrayalId, String name, Stat stat) throws BetrayalException {
		Player currentPlayer = getPlayer(betrayalId, name);
		currentPlayer.setStatIndex(stat, characterDataService.getStatBaseIndex(stat, currentPlayer.getCharacter()));
		playerRepository.save(currentPlayer);

		log.debug("Reset %s index for player with betrayalId: {} and name: {}", stat, betrayalId, name);
		return currentPlayer;
	}

	@Override
	public Player resetStatIndex(String betrayalId, Character character, Stat stat) throws BetrayalException {
		Player currentPlayer = getPlayer(betrayalId, character);
		currentPlayer.setStatIndex(stat, characterDataService.getStatBaseIndex(stat, character));
		playerRepository.save(currentPlayer);

		log.debug("Reset %s index for player with betrayalId: {} and character: {}", stat, betrayalId, character.toString());
		return currentPlayer;
	}

	@Override
	public Player incrementStatIndex(String betrayalId, String name, Stat stat) throws BetrayalException {
		Player currentPlayer = getPlayer(betrayalId, name);
		int currentStatIndex = currentPlayer.getStatIndex(stat);

		// Can't increment stat beyond max index
		if (currentStatIndex == BetrayalConst.MAX_STAT_INDEX) {
			throw new BetrayalException(String.format("Cannot increment %s beyond max index for betrayalId: %s, player: %s", stat, betrayalId, name),
					Response.Status.BAD_REQUEST);
		}

		// Increment stat index
		currentStatIndex++;
		currentPlayer.setStatIndex(stat, currentStatIndex);
		playerRepository.save(currentPlayer);

		log.debug("Incremented %s index for player with betrayalId: {} and name: {}", stat, betrayalId, name);
		return currentPlayer;
	}

	@Override
	public Player incrementStatIndex(String betrayalId, Character character, Stat stat) throws BetrayalException {
		Player currentPlayer = getPlayer(betrayalId, character);
		int currentStatIndex = currentPlayer.getStatIndex(stat);

		// Can't increment stat beyond max index
		if (currentStatIndex == BetrayalConst.MAX_STAT_INDEX) {
			throw new BetrayalException(String.format("Cannot increment %s beyond max index for betrayalId: %s, player: %s", stat, betrayalId, character),
					Response.Status.BAD_REQUEST);
		}

		// Increment stat index
		currentStatIndex++;
		currentPlayer.setStatIndex(stat, currentStatIndex);
		playerRepository.save(currentPlayer);

		log.debug("Incremented %s index for player with betrayalId: {} and character: {}", stat, betrayalId, character.toString());
		return currentPlayer;
	}

	@Override
	public Player decrementStatIndex(String betrayalId, String name, Stat stat) throws BetrayalException {
		Player currentPlayer = getPlayer(betrayalId, name);
		int currentStatIndex = currentPlayer.getStatIndex(stat);

		// Can't decrement stat beyond 0
		if (currentStatIndex == 0) {
			throw new BetrayalException(String.format("Cannot decrement %s index beyond 0 for betrayalId: %s, player: %s", stat, betrayalId, name),
					Response.Status.BAD_REQUEST);
		}

		// Decrement stat index
		currentStatIndex--;
		currentPlayer.setStatIndex(stat, currentStatIndex);

		// If stat index is now 0, set player as dead
		if (currentStatIndex == 0) {
			currentPlayer.setIsAlive(false);
			log.debug("Decrementing %s index for player with betrayalId: {} and name: {} will result in their death", stat, betrayalId, name);
		}

		playerRepository.save(currentPlayer);

		log.debug("Decremented %s index for player with betrayalId: {} and name: {}", stat, betrayalId, name);
		return currentPlayer;
	}

	@Override
	public Player decrementStatIndex(String betrayalId, Character character, Stat stat) throws BetrayalException {
		Player currentPlayer = getPlayer(betrayalId, character);
		int currentStatIndex = currentPlayer.getStatIndex(stat);

		// Can't decrement stat beyond 0
		if (currentStatIndex == 0) {
			throw new BetrayalException(String.format("Cannot decrement %s index beyond 0 for betrayalId: %s, player: %s", stat, betrayalId, character),
					Response.Status.BAD_REQUEST);
		}

		// Decrement stat index
		currentStatIndex--;
		currentPlayer.setStatIndex(stat, currentStatIndex);

		// If stat index is now 0, set player as dead
		if (currentStatIndex == 0) {
			currentPlayer.setIsAlive(false);
			log.debug("Decrementing %s index for player with betrayalId: {} and character: {} will result in their death", stat, betrayalId, character.toString());
		}

		playerRepository.save(currentPlayer);

		log.debug("Decremented %s index for player with betrayalId: {} and character: {}", stat, betrayalId, character.toString());
		return currentPlayer;
	}

	@Override
	public void deletePlayer(final String betrayalId, final String name) throws BetrayalException {
		List<Player> deletedPlayers = playerRepository.deleteByBetrayalIdAndName(betrayalId, name);
		if (deletedPlayers.isEmpty()) {
			throw new BetrayalException(String.format("Cannot delete player; player with betrayalId: %s and name: %s does not exist", betrayalId, name),
					Response.Status.NOT_FOUND);
		}

		log.debug("Deleted Player with betrayalId: {} and name: {}", betrayalId, name);
	}

	@Override
	public void deletePlayer(final String betrayalId, final Character character) throws BetrayalException {
		List<Player> deletedPlayers = playerRepository.deleteByBetrayalIdAndCharacter(betrayalId, character);
		if (deletedPlayers.isEmpty()) {
			throw new BetrayalException(String.format("Cannot delete player; player with betrayalId: %s and character: %s does not exist", betrayalId, character),
					Response.Status.NOT_FOUND);
		}

		log.debug("Deleted Player with betrayalId: {} and character: {}", betrayalId, character.toString());
	}

	@Override
	public void deleteBetrayalGame(final String betrayalId) throws BetrayalException {
		// Delete betrayal game
		List<Betrayal> deletedGames = betrayalRepository.deleteByBetrayalId(betrayalId);
		if (deletedGames.isEmpty()) {
			throw new BetrayalException(String.format("Cannot delete betrayal game; game with betrayalId: %s does not exist", betrayalId),
					Response.Status.NOT_FOUND);
		}

		// Cascade delete all players for game
		playerRepository.deleteAllByBetrayalId(betrayalId);

		log.debug("Deleted Betrayal game with id: {}, and all associated players with that betrayalId", betrayalId);
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