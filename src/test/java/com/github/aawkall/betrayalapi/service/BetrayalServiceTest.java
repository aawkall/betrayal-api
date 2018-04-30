package com.github.aawkall.betrayalapi.service;

import java.net.UnknownHostException;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.aawkall.betrayalapi.config.TestContext;
import com.github.aawkall.betrayalapi.entity.db.Betrayal;
import com.github.aawkall.betrayalapi.entity.db.Player;
import com.github.aawkall.betrayalapi.entity.db.Player.Character;
import com.github.aawkall.betrayalapi.exception.BetrayalException;
import com.github.aawkall.betrayalapi.util.BetrayalConst;
import com.github.aawkall.betrayalapi.util.BetrayalConst.Stat;
import com.mongodb.MongoClient;

@ContextConfiguration(classes = TestContext.class)
@TestExecutionListeners(inheritListeners = false, value = { DependencyInjectionTestExecutionListener.class })
public class BetrayalServiceTest extends AbstractTestNGSpringContextTests {

	private static final String CHANNEL_ID = "TestBetrayalId";
	private static final String ALTERNATE_CHANNEL_ID = "FakeId";
	private static final int HAUNT_NUMBER = 15;

	@Inject
	private BetrayalService betrayalService;

	@Inject
	private CharacterDataService characterDataService;

	private MongoTemplate testMongoTemplate;

	@BeforeClass
	private void setupTestMongo() throws UnknownHostException {
		testMongoTemplate = new MongoTemplate(new SimpleMongoDbFactory(new MongoClient(BetrayalConst.DOCKER_MONGO_HOST_TEST), BetrayalConst.DB_NAME));
	}

	@AfterMethod
	private void clearMongoDb() {
		testMongoTemplate.dropCollection(BetrayalConst.PLAYER_COLLECTION);
		testMongoTemplate.dropCollection(BetrayalConst.BETRAYAL_COLLECTION);
	}

	@Test
	public void testCreateBetrayalGame_conflict() throws BetrayalException {
		// Create betrayal game, then create another with the same betrayalId
		betrayalService.createBetrayalGame(CHANNEL_ID);

		try {
			betrayalService.createBetrayalGame(CHANNEL_ID);
			Assert.fail("CreateBetrayalGame with same betrayalId should have thrown an exception");
		} catch (BetrayalException e) {
			Assert.assertEquals(e.getHttpCode(), Response.Status.CONFLICT);
		}
	}

	@Test
	public void testCreateBetrayalGame_success() throws BetrayalException {
		Betrayal createBetrayal = betrayalService.createBetrayalGame(CHANNEL_ID);

		// Get Betrayal from the DB, verify info is the same
		Betrayal getBetrayal = betrayalService.getBetrayalGame(CHANNEL_ID);
		Assert.assertEquals(getBetrayal, createBetrayal);

		// Set and get haunt number, assert equal
		betrayalService.setHauntNumber(CHANNEL_ID, HAUNT_NUMBER);
		Assert.assertEquals(betrayalService.getHauntNumber(CHANNEL_ID), HAUNT_NUMBER);
	}

	@Test
	public void testGetBetrayalGame_betrayalIdNotFound() throws BetrayalException {
		// Create alternate betrayal game, to ensure query is using betrayalId
		betrayalService.createBetrayalGame(ALTERNATE_CHANNEL_ID);

		try {
			// Get betrayal game with nonexistent betrayalId
			betrayalService.getBetrayalGame(CHANNEL_ID);
			Assert.fail("GetBetrayalGame with nonexistent betrayalId should have thrown an exception");
		} catch (BetrayalException e) {
			Assert.assertEquals(e.getHttpCode(), Response.Status.NOT_FOUND);
		}
	}

	@Test
	public void testSetHauntNumber_outOfRange() throws BetrayalException {
		try {
			// Set haunt number to number out of range
			betrayalService.setHauntNumber(CHANNEL_ID, 300);
			Assert.fail("SetHauntNumber with haunt number out of range should have thrown an exception");
		} catch (BetrayalException e) {
			Assert.assertEquals(e.getHttpCode(), Response.Status.BAD_REQUEST);
		}
	}

	@Test
	public void testAddPlayer_nameAlreadyExists() throws BetrayalException {
		// Add player with name, then try to add again with same name but different character
		betrayalService.createBetrayalGame(CHANNEL_ID);
		betrayalService.addPlayer(CHANNEL_ID, Character.ZOE_INGSTROM, "Aaron");

		try {
			betrayalService.addPlayer(CHANNEL_ID, Character.BRANDON_JASPERS, "Aaron");
			Assert.fail("AddPlayer with existing name within given betrayalId should have thrown an exception");
		} catch (BetrayalException e) {
			Assert.assertEquals(e.getHttpCode(), Response.Status.CONFLICT);
		}
	}

	@Test
	public void testAddPlayer_characterAlreadyExists() throws BetrayalException {
		// Add player with character, then try to add again with same character but different name
		betrayalService.createBetrayalGame(CHANNEL_ID);
		betrayalService.addPlayer(CHANNEL_ID, Character.ZOE_INGSTROM, "Aaron");

		try {
			betrayalService.addPlayer(CHANNEL_ID, Character.ZOE_INGSTROM, "Chris");
			Assert.fail("AddPlayer with existing character within given betrayalId should have thrown an exception");
		} catch (BetrayalException e) {
			Assert.assertEquals(e.getHttpCode(), Response.Status.CONFLICT);
		}
	}

	@Test
	public void testAddPlayer_flipSideAlreadyExists() throws BetrayalException {
		// Add player with flipside character, then try to add again with character from other side
		betrayalService.createBetrayalGame(CHANNEL_ID);
		betrayalService.addPlayer(CHANNEL_ID, Character.ZOE_INGSTROM, "Aaron");

		try {
			betrayalService.addPlayer(CHANNEL_ID, Character.MISSY_DUBOURDE, "Chris");
			Assert.fail("AddPlayer with flipside character within given betrayalId should have thrown an exception");
		} catch (BetrayalException e) {
			Assert.assertEquals(e.getHttpCode(), Response.Status.CONFLICT);
		}
	}

	@Test
	public void testAddPlayer_success() throws BetrayalException {
		// Create game and add player
		betrayalService.createBetrayalGame(CHANNEL_ID);
		Player addPlayer = betrayalService.addPlayer(CHANNEL_ID, Character.ZOE_INGSTROM, "Aaron");

		// Get Player from the DB via name, verify info is the same and verify stats are at base level
		Player namePlayer = betrayalService.getPlayer(CHANNEL_ID, "Aaron");
		Assert.assertEquals(namePlayer, addPlayer);
		Assert.assertEquals(namePlayer.getSpeedIndex(), characterDataService.getStatBaseIndex(Stat.SPEED, Character.ZOE_INGSTROM));
		Assert.assertEquals(namePlayer.getMightIndex(), characterDataService.getStatBaseIndex(Stat.MIGHT, Character.ZOE_INGSTROM));
		Assert.assertEquals(namePlayer.getSanityIndex(), characterDataService.getStatBaseIndex(Stat.SANITY, Character.ZOE_INGSTROM));
		Assert.assertEquals(namePlayer.getKnowledgeIndex(), characterDataService.getStatBaseIndex(Stat.KNOWLEDGE, Character.ZOE_INGSTROM));

		// Get Player from the DB via character, verify info is the same (only need to verify stats once)
		Player characterPlayer = betrayalService.getPlayer(CHANNEL_ID, Character.ZOE_INGSTROM);
		Assert.assertEquals(characterPlayer, addPlayer);
	}

	@Test
	public void testAddPlayer_sameDataDifferentBetrayalId() throws BetrayalException {
		// Create two betrayal games, add the same player to both, and ensure no exceptions are thrown
		betrayalService.createBetrayalGame(CHANNEL_ID);
		betrayalService.createBetrayalGame(ALTERNATE_CHANNEL_ID);
		betrayalService.addPlayer(CHANNEL_ID, Character.ZOE_INGSTROM, "Aaron");
		betrayalService.addPlayer(ALTERNATE_CHANNEL_ID, Character.ZOE_INGSTROM, "Aaron");
	}

	@Test
	public void testGetPlayer_nameNotFound() throws BetrayalException {
		// Create game and player with a different name
		betrayalService.createBetrayalGame(CHANNEL_ID);
		betrayalService.addPlayer(CHANNEL_ID, Character.ZOE_INGSTROM, "Aaron");

		try {
			// Get player with nonexistent name
			betrayalService.getPlayer(CHANNEL_ID, "NoName");
			Assert.fail("GetPlayer with nonexistent name within given betrayalId should have thrown an exception");
		} catch (BetrayalException e) {
			Assert.assertEquals(e.getHttpCode(), Response.Status.NOT_FOUND);
		}
	}

	@Test
	public void testGetPlayer_characterNotFound() throws BetrayalException {
		// Create game and player with a different character
		betrayalService.createBetrayalGame(CHANNEL_ID);
		betrayalService.addPlayer(CHANNEL_ID, Character.ZOE_INGSTROM, "Aaron");

		try {
			// Get player with other nonexistent character
			betrayalService.getPlayer(CHANNEL_ID, Character.BRANDON_JASPERS);
			Assert.fail("GetPlayer with nonexistent character within given betrayalId should have thrown an exception");
		} catch (BetrayalException e) {
			Assert.assertEquals(e.getHttpCode(), Response.Status.NOT_FOUND);
		}
	}

	@Test
	public void testGetAllPlayers_success() throws BetrayalException {
		// Create game and add 2 players
		betrayalService.createBetrayalGame(CHANNEL_ID);
		Player player1 = betrayalService.addPlayer(CHANNEL_ID, Character.ZOE_INGSTROM, "Aaron");
		Player player2 = betrayalService.addPlayer(CHANNEL_ID, Character.BRANDON_JASPERS, "Chris");

		// Get all players, verify size and player data
		List<Player> players = betrayalService.getAllPlayers(CHANNEL_ID);
		Assert.assertEquals(players.size(), 2);
		Assert.assertTrue(players.contains(player1));
		Assert.assertTrue(players.contains(player2));
	}

	@Test
	public void testGetAllPlayers_empty() throws BetrayalException {
		// Create game but don't add any players
		betrayalService.createBetrayalGame(CHANNEL_ID);

		// Create alternate game with one player
		betrayalService.createBetrayalGame(ALTERNATE_CHANNEL_ID);
		betrayalService.addPlayer(ALTERNATE_CHANNEL_ID, Character.BRANDON_JASPERS, "Aaron");

		// Get all players for initial betrayalId, verify size is 0
		List<Player> players = betrayalService.getAllPlayers(CHANNEL_ID);
		Assert.assertEquals(players.size(), 0);
	}

	@Test
	public void testGetAllTraitors_success() throws BetrayalException {
		// Create game and add 2 players, marking only one as a traitor
		betrayalService.createBetrayalGame(CHANNEL_ID);
		Player hero = betrayalService.addPlayer(CHANNEL_ID, Character.ZOE_INGSTROM, "Aaron");
		Player traitor = betrayalService.addPlayer(CHANNEL_ID, Character.BRANDON_JASPERS, "Chris");
		traitor = betrayalService.markAsTraitor(CHANNEL_ID, traitor.getCharacter());

		// Get all traitors, verify size and players in the list
		List<Player> traitors = betrayalService.getAllTraitors(CHANNEL_ID);
		Assert.assertEquals(traitors.size(), 1);
		Assert.assertTrue(traitors.contains(traitor));
		Assert.assertTrue(!traitors.contains(hero));
	}

	@Test
	public void testGetAllTraitors_empty() throws BetrayalException {
		// Create game, adding only one player, but leaving them as a hero
		betrayalService.createBetrayalGame(CHANNEL_ID);
		betrayalService.addPlayer(CHANNEL_ID, Character.ZOE_INGSTROM, "Aaron");

		// Get all traitors, verify size is 0
		List<Player> traitors = betrayalService.getAllTraitors(CHANNEL_ID);
		Assert.assertEquals(traitors.size(), 0);
	}

	@Test
	public void testGetAllHeroes_success() throws BetrayalException {
		// Create game and add 2 players, marking only one as a traitor
		betrayalService.createBetrayalGame(CHANNEL_ID);
		Player hero = betrayalService.addPlayer(CHANNEL_ID, Character.ZOE_INGSTROM, "Aaron");
		Player traitor = betrayalService.addPlayer(CHANNEL_ID, Character.BRANDON_JASPERS, "Chris");
		traitor = betrayalService.markAsTraitor(CHANNEL_ID, traitor.getCharacter());

		// Get all heroes, verify size and players in the list
		List<Player> heroes = betrayalService.getAllHeroes(CHANNEL_ID);
		Assert.assertEquals(heroes.size(), 1);
		Assert.assertTrue(!heroes.contains(traitor));
		Assert.assertTrue(heroes.contains(hero));
	}

	@Test
	public void testGetAllHeroes_empty() throws BetrayalException {
		// Create game, adding only one player, and mark them as a traitor
		betrayalService.createBetrayalGame(CHANNEL_ID);
		Player traitor = betrayalService.addPlayer(CHANNEL_ID, Character.ZOE_INGSTROM, "Aaron");
		betrayalService.markAsTraitor(CHANNEL_ID, traitor.getCharacter());

		// Get all heroes, verify size is 0
		List<Player> heroes = betrayalService.getAllHeroes(CHANNEL_ID);
		Assert.assertEquals(heroes.size(), 0);
	}

	@Test
	public void testGetAllAlive_success() throws BetrayalException {
		// Create game and add 2 players, marking only one as dead
		betrayalService.createBetrayalGame(CHANNEL_ID);
		Player alive = betrayalService.addPlayer(CHANNEL_ID, Character.ZOE_INGSTROM, "Aaron");
		Player dead = betrayalService.addPlayer(CHANNEL_ID, Character.BRANDON_JASPERS, "Chris");
		dead = betrayalService.markAsDead(CHANNEL_ID, dead.getCharacter());

		// Get all alive, verify size and players in the list
		List<Player> living = betrayalService.getAllAlive(CHANNEL_ID);
		Assert.assertEquals(living.size(), 1);
		Assert.assertTrue(!living.contains(dead));
		Assert.assertTrue(living.contains(alive));
	}

	@Test
	public void testGetAllAlive_empty() throws BetrayalException {
		// Create game, adding only one player, and mark them as dead
		betrayalService.createBetrayalGame(CHANNEL_ID);
		Player dead = betrayalService.addPlayer(CHANNEL_ID, Character.ZOE_INGSTROM, "Aaron");
		betrayalService.markAsDead(CHANNEL_ID, dead.getCharacter());

		// Get all living, verify size is 0
		List<Player> living = betrayalService.getAllAlive(CHANNEL_ID);
		Assert.assertEquals(living.size(), 0);
	}

	@Test
	public void testGetAllDead_success() throws BetrayalException {
		// Create game and add 2 players, marking only one as dead
		betrayalService.createBetrayalGame(CHANNEL_ID);
		Player alive = betrayalService.addPlayer(CHANNEL_ID, Character.ZOE_INGSTROM, "Aaron");
		Player dead = betrayalService.addPlayer(CHANNEL_ID, Character.BRANDON_JASPERS, "Chris");
		dead = betrayalService.markAsDead(CHANNEL_ID, dead.getCharacter());

		// Get all of the dead, verify size and players in the list
		List<Player> theDead = betrayalService.getAllDead(CHANNEL_ID);
		Assert.assertEquals(theDead.size(), 1);
		Assert.assertTrue(theDead.contains(dead));
		Assert.assertTrue(!theDead.contains(alive));
	}

	@Test
	public void testGetAllDead_empty() throws BetrayalException {
		// Create game, adding only one player, leaving them as alive
		betrayalService.createBetrayalGame(CHANNEL_ID);
		betrayalService.addPlayer(CHANNEL_ID, Character.ZOE_INGSTROM, "Aaron");

		// Get all dead, verify size is 0
		List<Player> dead = betrayalService.getAllDead(CHANNEL_ID);
		Assert.assertEquals(dead.size(), 0);
	}

	@Test
	public void testMarkAsDeadAlive_name() throws BetrayalException {
		// Create game and add player
		String playerName = "Aaron";
		betrayalService.createBetrayalGame(CHANNEL_ID);
		betrayalService.addPlayer(CHANNEL_ID, Character.ZOE_INGSTROM, playerName);

		// Mark player as dead via name, then get player to verify
		betrayalService.markAsDead(CHANNEL_ID, playerName);
		Assert.assertFalse(betrayalService.getPlayer(CHANNEL_ID, playerName).getIsAlive());

		// Mark player as alive via name, then get player to verify
		betrayalService.markAsAlive(CHANNEL_ID, playerName);
		Assert.assertTrue(betrayalService.getPlayer(CHANNEL_ID, playerName).getIsAlive());
	}

	@Test
	public void testMarkAsDeadAlive_character() throws BetrayalException {
		// Create game and add player
		Character playerCharacter = Character.BRANDON_JASPERS;
		betrayalService.createBetrayalGame(CHANNEL_ID);
		betrayalService.addPlayer(CHANNEL_ID, playerCharacter, "Aaron");

		// Mark player as dead via character, then get player to verify
		betrayalService.markAsDead(CHANNEL_ID, playerCharacter);
		Assert.assertFalse(betrayalService.getPlayer(CHANNEL_ID, playerCharacter).getIsAlive());

		// Mark player as alive via character, then get player to verify
		betrayalService.markAsAlive(CHANNEL_ID, playerCharacter);
		Assert.assertTrue(betrayalService.getPlayer(CHANNEL_ID, playerCharacter).getIsAlive());
	}

	@Test
	public void testMarkAsTraitorHero_name() throws BetrayalException {
		// Create game and add player
		String playerName = "Aaron";
		betrayalService.createBetrayalGame(CHANNEL_ID);
		betrayalService.addPlayer(CHANNEL_ID, Character.ZOE_INGSTROM, playerName);

		// Mark player as traitor via name, then get player to verify
		betrayalService.markAsTraitor(CHANNEL_ID, playerName);
		Assert.assertTrue(betrayalService.getPlayer(CHANNEL_ID, playerName).getIsTraitor());

		// Mark player as hero via name, then get player to verify
		betrayalService.markAsHero(CHANNEL_ID, playerName);
		Assert.assertFalse(betrayalService.getPlayer(CHANNEL_ID, playerName).getIsTraitor());
	}

	@Test
	public void testMarkAsTraitorHero_character() throws BetrayalException {
		// Create game and add player
		Character playerCharacter = Character.BRANDON_JASPERS;
		betrayalService.createBetrayalGame(CHANNEL_ID);
		betrayalService.addPlayer(CHANNEL_ID, playerCharacter, "Aaron");

		// Mark player as traitor via character, then get player to verify
		betrayalService.markAsTraitor(CHANNEL_ID, playerCharacter);
		Assert.assertTrue(betrayalService.getPlayer(CHANNEL_ID, playerCharacter).getIsTraitor());

		// Mark player as hero via character, then get player to verify
		betrayalService.markAsHero(CHANNEL_ID, playerCharacter);
		Assert.assertFalse(betrayalService.getPlayer(CHANNEL_ID, playerCharacter).getIsTraitor());
	}

	@Test
	public void testIncrementStatIndex_pastMax() throws BetrayalException {
		// Create Betrayal game, add player
		betrayalService.createBetrayalGame(CHANNEL_ID);
		Player player = betrayalService.addPlayer(CHANNEL_ID, Character.BRANDON_JASPERS, "Aaron");

		// Loop incrementing a stat of the player until they try to increment past the max stat index
		// Try by name and by character
		try {
			for (int i = 0; i < BetrayalConst.MAX_STAT_INDEX; i++) {
				betrayalService.incrementStatIndex(CHANNEL_ID, player.getName(), Stat.SPEED);
			}
			Assert.fail("IncrementStatIndex should have thrown an exception when incrementing a stat index by name past max index");
		} catch (BetrayalException e) {
			Assert.assertEquals(e.getHttpCode(), Response.Status.BAD_REQUEST);
		}
		try {
			betrayalService.incrementStatIndex(CHANNEL_ID, player.getCharacter(), Stat.SPEED);
			Assert.fail("IncrementStatIndex should have thrown an exception when incrementing a stat index by character past max index");
		} catch (BetrayalException e) {
			Assert.assertEquals(e.getHttpCode(), Response.Status.BAD_REQUEST);
		}
	}

	@Test
	public void testIncrementStatIndex_success() throws BetrayalException {
		// Create Betrayal game, add player
		betrayalService.createBetrayalGame(CHANNEL_ID);
		Player addPlayer = betrayalService.addPlayer(CHANNEL_ID, Character.BRANDON_JASPERS, "Aaron");

		// Increment each of their stats twice, using both name and character
		betrayalService.incrementStatIndex(CHANNEL_ID, addPlayer.getName(), Stat.SPEED);
		betrayalService.incrementStatIndex(CHANNEL_ID, addPlayer.getName(), Stat.MIGHT);
		betrayalService.incrementStatIndex(CHANNEL_ID, addPlayer.getName(), Stat.SANITY);
		betrayalService.incrementStatIndex(CHANNEL_ID, addPlayer.getName(), Stat.KNOWLEDGE);
		betrayalService.incrementStatIndex(CHANNEL_ID, addPlayer.getCharacter(), Stat.SPEED);
		betrayalService.incrementStatIndex(CHANNEL_ID, addPlayer.getCharacter(), Stat.MIGHT);
		betrayalService.incrementStatIndex(CHANNEL_ID, addPlayer.getCharacter(), Stat.SANITY);
		betrayalService.incrementStatIndex(CHANNEL_ID, addPlayer.getCharacter(), Stat.KNOWLEDGE);

		// Get the player, ensure stat values are incremented by 2
		Player getPlayer = betrayalService.getPlayer(CHANNEL_ID, addPlayer.getCharacter());
		Assert.assertEquals(getPlayer.getSpeedIndex(), addPlayer.getSpeedIndex() + 2);
		Assert.assertEquals(getPlayer.getMightIndex(), addPlayer.getMightIndex() + 2);
		Assert.assertEquals(getPlayer.getSanityIndex(), addPlayer.getSanityIndex() + 2);
		Assert.assertEquals(getPlayer.getKnowledgeIndex(), addPlayer.getKnowledgeIndex() + 2);
	}

	@Test
	public void testDecrementStatIndex_pastZero() throws BetrayalException {
		// Create Betrayal game, add player
		betrayalService.createBetrayalGame(CHANNEL_ID);
		Player player = betrayalService.addPlayer(CHANNEL_ID, Character.BRANDON_JASPERS, "Aaron");

		// Loop decrementing a stat of the player until they try to decrement past zero
		// Try by name and by character
		try {
			for (int i = 0; i < BetrayalConst.MAX_STAT_INDEX; i++) {
				betrayalService.decrementStatIndex(CHANNEL_ID, player.getName(), Stat.SPEED);
			}
			Assert.fail("DecrementStatIndex should have thrown an exception when decrementing a stat index by name past zero");
		} catch (BetrayalException e) {
			Assert.assertEquals(e.getHttpCode(), Response.Status.BAD_REQUEST);
		}
		try {
			betrayalService.decrementStatIndex(CHANNEL_ID, player.getCharacter(), Stat.SPEED);
			Assert.fail("DecrementStatIndex should have thrown an exception when decrementing a stat index by character past zero");
		} catch (BetrayalException e) {
			Assert.assertEquals(e.getHttpCode(), Response.Status.BAD_REQUEST);
		}
	}

	@Test
	public void testDecrementStatIndex_success() throws BetrayalException {
		// Create Betrayal game, add player
		betrayalService.createBetrayalGame(CHANNEL_ID);
		Player addPlayer = betrayalService.addPlayer(CHANNEL_ID, Character.BRANDON_JASPERS, "Aaron");

		// Decrement each of their stats twice, using both name and character
		betrayalService.decrementStatIndex(CHANNEL_ID, addPlayer.getName(), Stat.SPEED);
		betrayalService.decrementStatIndex(CHANNEL_ID, addPlayer.getName(), Stat.MIGHT);
		betrayalService.decrementStatIndex(CHANNEL_ID, addPlayer.getName(), Stat.SANITY);
		betrayalService.decrementStatIndex(CHANNEL_ID, addPlayer.getName(), Stat.KNOWLEDGE);
		betrayalService.decrementStatIndex(CHANNEL_ID, addPlayer.getCharacter(), Stat.SPEED);
		betrayalService.decrementStatIndex(CHANNEL_ID, addPlayer.getCharacter(), Stat.MIGHT);
		betrayalService.decrementStatIndex(CHANNEL_ID, addPlayer.getCharacter(), Stat.SANITY);
		betrayalService.decrementStatIndex(CHANNEL_ID, addPlayer.getCharacter(), Stat.KNOWLEDGE);

		// Get the player, ensure stat values are decremented by 2 and player is still alive
		Player getPlayer = betrayalService.getPlayer(CHANNEL_ID, addPlayer.getCharacter());
		Assert.assertEquals(getPlayer.getSpeedIndex(), addPlayer.getSpeedIndex() - 2);
		Assert.assertEquals(getPlayer.getMightIndex(), addPlayer.getMightIndex() - 2);
		Assert.assertEquals(getPlayer.getSanityIndex(), addPlayer.getSanityIndex() - 2);
		Assert.assertEquals(getPlayer.getKnowledgeIndex(), addPlayer.getKnowledgeIndex() - 2);
		Assert.assertTrue(getPlayer.getIsAlive());

		// Decrement one more time to make Brandon's speed go to zero, verify is dead
		betrayalService.decrementStatIndex(CHANNEL_ID, getPlayer.getCharacter(), Stat.SPEED);
		Player deadPlayer = betrayalService.getPlayer(CHANNEL_ID, getPlayer.getCharacter());
		Assert.assertEquals(deadPlayer.getSpeedIndex(), 0);
		Assert.assertFalse(deadPlayer.getIsAlive());
	}

	@Test
	public void testResetStatIndex_success() throws BetrayalException {
		// Create Betrayal game, add player and keep for initial stat values
		betrayalService.createBetrayalGame(CHANNEL_ID);
		Player addPlayer = betrayalService.addPlayer(CHANNEL_ID, Character.BRANDON_JASPERS, "Aaron");

		// Increment each of their stats, then reset stat index for each (using both name and character)
		betrayalService.incrementStatIndex(CHANNEL_ID, addPlayer.getName(), Stat.SPEED);
		betrayalService.incrementStatIndex(CHANNEL_ID, addPlayer.getName(), Stat.MIGHT);
		betrayalService.incrementStatIndex(CHANNEL_ID, addPlayer.getCharacter(), Stat.SANITY);
		betrayalService.incrementStatIndex(CHANNEL_ID, addPlayer.getCharacter(), Stat.KNOWLEDGE);
		betrayalService.resetStatIndex(CHANNEL_ID, addPlayer.getCharacter(), Stat.SPEED);
		betrayalService.resetStatIndex(CHANNEL_ID, addPlayer.getCharacter(), Stat.MIGHT);
		betrayalService.resetStatIndex(CHANNEL_ID, addPlayer.getName(), Stat.SANITY);
		betrayalService.resetStatIndex(CHANNEL_ID, addPlayer.getName(), Stat.KNOWLEDGE);

		// Get player again, and verify their stats match the initial index
		Player getPlayer = betrayalService.getPlayer(CHANNEL_ID, addPlayer.getCharacter());
		Assert.assertEquals(getPlayer.getSpeedIndex(), addPlayer.getSpeedIndex());
		Assert.assertEquals(getPlayer.getMightIndex(), addPlayer.getMightIndex());
		Assert.assertEquals(getPlayer.getSanityIndex(), addPlayer.getSanityIndex());
		Assert.assertEquals(getPlayer.getKnowledgeIndex(), addPlayer.getKnowledgeIndex());
	}

	@Test
	public void testDeletePlayer_nameNotFound() throws BetrayalException {
		// Create game and player with a different name
		betrayalService.createBetrayalGame(CHANNEL_ID);
		betrayalService.addPlayer(CHANNEL_ID, Character.ZOE_INGSTROM, "Aaron");

		try {
			// Delete player with nonexistent name
			betrayalService.deletePlayer(CHANNEL_ID, "NoName");
			Assert.fail("DeletePlayer with nonexistent name within given betrayalId should have thrown an exception");
		} catch (BetrayalException e) {
			Assert.assertEquals(e.getHttpCode(), Response.Status.NOT_FOUND);
		}
	}

	@Test
	public void testDeletePlayer_characterNotFound() throws BetrayalException {
		// Create game and player with a different character
		betrayalService.createBetrayalGame(CHANNEL_ID);
		betrayalService.addPlayer(CHANNEL_ID, Character.ZOE_INGSTROM, "Aaron");

		try {
			// Delete player with nonexistent character
			betrayalService.deletePlayer(CHANNEL_ID, Character.BRANDON_JASPERS);
			Assert.fail("DeletePlayer with nonexistent character within given betrayalId should have thrown an exception");
		} catch (BetrayalException e) {
			Assert.assertEquals(e.getHttpCode(), Response.Status.NOT_FOUND);
		}
	}

	@Test
	public void testDeletePlayer_success() throws BetrayalException {
		// Create game and add three players
		betrayalService.createBetrayalGame(CHANNEL_ID);
		Player deleteByName = betrayalService.addPlayer(CHANNEL_ID, Character.ZOE_INGSTROM, "Aaron");
		Player deleteByCharacter = betrayalService.addPlayer(CHANNEL_ID, Character.VIVIAN_LOPEZ, "Chris");
		Player getsToStay = betrayalService.addPlayer(CHANNEL_ID, Character.HEATHER_GRANVILLE, "Laura");

		// Delete one by name, another by character, leave one
		betrayalService.deletePlayer(CHANNEL_ID, deleteByName.getName());
		betrayalService.deletePlayer(CHANNEL_ID, deleteByCharacter.getCharacter());

		// Ensure deleted players are not found
		try {
			betrayalService.getPlayer(CHANNEL_ID, deleteByName.getName());
			Assert.fail("GetPlayer should have thrown NOT_FOUND when trying to get Player deleted by name");
		} catch (BetrayalException e) {
			Assert.assertEquals(e.getHttpCode(), Response.Status.NOT_FOUND);
		}
		try {
			betrayalService.getPlayer(CHANNEL_ID, deleteByCharacter.getCharacter());
			Assert.fail("GetPlayer should have thrown NOT_FOUND when trying to get Player deleted by character");
		} catch (BetrayalException e) {
			Assert.assertEquals(e.getHttpCode(), Response.Status.NOT_FOUND);
		}

		// Ensure player that wasn't deleted still exists (can get player without throwing exception)
		betrayalService.getPlayer(CHANNEL_ID, getsToStay.getName());
	}

	@Test
	public void testDeleteBetrayalGame_betrayalIdNotFound() throws BetrayalException {
		// Create alternate betrayal game, to ensure query is using betrayalId
		betrayalService.createBetrayalGame(ALTERNATE_CHANNEL_ID);

		try {
			// Delete betrayal game with nonexistent betrayalId
			betrayalService.deleteBetrayalGame(CHANNEL_ID);
			Assert.fail("DeleteBetrayalGame with nonexistent betrayalId should have thrown an exception");
		} catch (BetrayalException e) {
			Assert.assertEquals(e.getHttpCode(), Response.Status.NOT_FOUND);
		}
	}

	@Test
	public void testDeleteBetrayalGame_success() throws BetrayalException {
		// Create 2 games with different betrayalIds, adding 2 players to each
		Betrayal toDelete = betrayalService.createBetrayalGame(CHANNEL_ID);
		Betrayal toKeep = betrayalService.createBetrayalGame(ALTERNATE_CHANNEL_ID);
		Player toDeletePlayer1 = betrayalService.addPlayer(CHANNEL_ID, Character.ZOE_INGSTROM, "Aaron");
		Player toDeletePlayer2 = betrayalService.addPlayer(CHANNEL_ID, Character.VIVIAN_LOPEZ, "Chris");
		Player toKeepPlayer1 = betrayalService.addPlayer(ALTERNATE_CHANNEL_ID, Character.HEATHER_GRANVILLE, "Laura");
		Player toKeepPlayer2 = betrayalService.addPlayer(ALTERNATE_CHANNEL_ID, Character.DARRIN_WILLIAMS, "Jonathan");

		// Delete one of the betrayal games, keeping the other
		betrayalService.deleteBetrayalGame(toDelete.getBetrayalId());

		// Ensure betrayal game is not found, and players were also deleted
		try {
			betrayalService.getBetrayalGame(toDelete.getBetrayalId());
			Assert.fail("GetBetrayalGame should have thrown NOT_FOUND when trying to get a deleted Betrayal game");
		} catch (BetrayalException e) {
			Assert.assertEquals(e.getHttpCode(), Response.Status.NOT_FOUND);
		}
		try {
			betrayalService.getPlayer(toDelete.getBetrayalId(), toDeletePlayer1.getName());
			Assert.fail("GetPlayer should have thrown NOT_FOUND when trying to get Player deleted via Betrayal delete");
		} catch (BetrayalException e) {
			Assert.assertEquals(e.getHttpCode(), Response.Status.NOT_FOUND);
		}
		try {
			betrayalService.getPlayer(toDelete.getBetrayalId(), toDeletePlayer2.getName());
			Assert.fail("GetPlayer should have thrown NOT_FOUND when trying to get Player deleted via Betrayal delete");
		} catch (BetrayalException e) {
			Assert.assertEquals(e.getHttpCode(), Response.Status.NOT_FOUND);
		}

		// Verify other betrayal game and players still exist (can get player and game without throwing exception)
		betrayalService.getBetrayalGame(toKeep.getBetrayalId());
		betrayalService.getPlayer(toKeep.getBetrayalId(), toKeepPlayer1.getName());
		betrayalService.getPlayer(toKeep.getBetrayalId(), toKeepPlayer2.getName());
	}
}