package com.github.aawkall.betrayalapi.resource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;

import javax.ws.rs.core.Response;

import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.aawkall.betrayalapi.entity.db.Player.Character;
import com.github.aawkall.betrayalapi.entity.request.AddPlayer;
import com.github.aawkall.betrayalapi.entity.request.CreateBetrayal;
import com.github.aawkall.betrayalapi.entity.request.StatRequest;
import com.github.aawkall.betrayalapi.entity.request.StatRequest.StatOperation;
import com.github.aawkall.betrayalapi.exception.BetrayalException;
import com.github.aawkall.betrayalapi.service.CharacterDataService;
import com.github.aawkall.betrayalapi.service.CharacterDataServiceImpl;
import com.github.aawkall.betrayalapi.util.BetrayalConst.Stat;

public class PlayerResourceTest {

	public static final String baseUrl = "http://localhost:8080/betrayal-api/betrayalapi";
	public static final String betrayalId = "TestBetrayalId";
	public static final String player1Name = "Player1";
	public static final String player2Name = "Player2";
	public static final Character player1Character = Character.BRANDON_JASPERS;
	public static final String player1CharacterString = player1Character.toString();
	public static final Character player2Character = Character.ZOE_INGSTROM;
	public static final String player2CharacterString = player2Character.toString();

	private int speedBaseIndex1;
	private int mightBaseIndex1;
	private int sanityBaseIndex1;
	private int knowledgeBaseIndex1;
	private int baseSpeedPlayer1;
	private int baseMightPlayer1;
	private int baseSanityPlayer1;
	private int baseKnowledgePlayer1;
	private int plusOneSpeedPlayer1;
	private int plusOneMightPlayer1;
	private int plusOneSanityPlayer1;
	private int plusOneKnowledgePlayer1;
	private int minusOneSpeedPlayer1;
	private int minusOneMightPlayer1;
	private int minusOneSanityPlayer1;
	private int minusOneKnowledgePlayer1;

	private int speedBaseIndex2;
	private int mightBaseIndex2;
	private int sanityBaseIndex2;
	private int knowledgeBaseIndex2;
	private int baseSpeedPlayer2;
	private int baseMightPlayer2;
	private int baseSanityPlayer2;
	private int baseKnowledgePlayer2;
	private int plusOneSpeedPlayer2;
	private int plusOneMightPlayer2;
	private int plusOneSanityPlayer2;
	private int plusOneKnowledgePlayer2;
	private int minusOneSpeedPlayer2;
	private int minusOneMightPlayer2;
	private int minusOneSanityPlayer2;
	private int minusOneKnowledgePlayer2;

	private CharacterDataService characterDataService = new CharacterDataServiceImpl();

	@BeforeClass
	public void setupInitialStats() throws BetrayalException {
		// Player1
		speedBaseIndex1 = characterDataService.getStatBaseIndex(Stat.SPEED, player1Character);
		mightBaseIndex1 = characterDataService.getStatBaseIndex(Stat.MIGHT, player1Character);
		sanityBaseIndex1 = characterDataService.getStatBaseIndex(Stat.SANITY, player1Character);
		knowledgeBaseIndex1 = characterDataService.getStatBaseIndex(Stat.KNOWLEDGE, player1Character);
		baseSpeedPlayer1 = characterDataService.getStatValue(Stat.SPEED,
				speedBaseIndex1, player1Character);
		baseMightPlayer1 = characterDataService.getStatValue(Stat.MIGHT,
				mightBaseIndex1, player1Character);
		baseSanityPlayer1 = characterDataService.getStatValue(Stat.SANITY,
				sanityBaseIndex1, player1Character);
		baseKnowledgePlayer1 = characterDataService.getStatValue(Stat.KNOWLEDGE,
				knowledgeBaseIndex1, player1Character);
		plusOneSpeedPlayer1 = characterDataService.getStatValue(Stat.SPEED,
				speedBaseIndex1 + 1, player1Character);
		plusOneMightPlayer1 = characterDataService.getStatValue(Stat.MIGHT,
				mightBaseIndex1 + 1, player1Character);
		plusOneSanityPlayer1 = characterDataService.getStatValue(Stat.SANITY,
				sanityBaseIndex1 + 1, player1Character);
		plusOneKnowledgePlayer1 = characterDataService.getStatValue(Stat.KNOWLEDGE,
				knowledgeBaseIndex1 + 1, player1Character);
		minusOneSpeedPlayer1 = characterDataService.getStatValue(Stat.SPEED,
				speedBaseIndex1 - 1, player1Character);
		minusOneMightPlayer1 = characterDataService.getStatValue(Stat.MIGHT,
				mightBaseIndex1 - 1, player1Character);
		minusOneSanityPlayer1 = characterDataService.getStatValue(Stat.SANITY,
				sanityBaseIndex1 - 1, player1Character);
		minusOneKnowledgePlayer1 = characterDataService.getStatValue(Stat.KNOWLEDGE,
				knowledgeBaseIndex1 - 1, player1Character);

		// Player2
		speedBaseIndex2 = characterDataService.getStatBaseIndex(Stat.SPEED, player2Character);
		mightBaseIndex2 = characterDataService.getStatBaseIndex(Stat.MIGHT, player2Character);
		sanityBaseIndex2 = characterDataService.getStatBaseIndex(Stat.SANITY, player2Character);
		knowledgeBaseIndex2 = characterDataService.getStatBaseIndex(Stat.KNOWLEDGE, player2Character);
		baseSpeedPlayer2 = characterDataService.getStatValue(Stat.SPEED,
				speedBaseIndex2, player2Character);
		baseMightPlayer2 = characterDataService.getStatValue(Stat.MIGHT,
				mightBaseIndex2, player2Character);
		baseSanityPlayer2 = characterDataService.getStatValue(Stat.SANITY,
				sanityBaseIndex2, player2Character);
		baseKnowledgePlayer2 = characterDataService.getStatValue(Stat.KNOWLEDGE,
				knowledgeBaseIndex2, player2Character);
		plusOneSpeedPlayer2 = characterDataService.getStatValue(Stat.SPEED,
				speedBaseIndex2 + 1, player2Character);
		plusOneMightPlayer2 = characterDataService.getStatValue(Stat.MIGHT,
				mightBaseIndex2 + 1, player2Character);
		plusOneSanityPlayer2 = characterDataService.getStatValue(Stat.SANITY,
				sanityBaseIndex2 + 1, player2Character);
		plusOneKnowledgePlayer2 = characterDataService.getStatValue(Stat.KNOWLEDGE,
				knowledgeBaseIndex2 + 1, player2Character);
		minusOneSpeedPlayer2 = characterDataService.getStatValue(Stat.SPEED,
				speedBaseIndex2 - 1, player2Character);
		minusOneMightPlayer2 = characterDataService.getStatValue(Stat.MIGHT,
				mightBaseIndex2 - 1, player2Character);
		minusOneSanityPlayer2 = characterDataService.getStatValue(Stat.SANITY,
				sanityBaseIndex2 - 1, player2Character);
		minusOneKnowledgePlayer2 = characterDataService.getStatValue(Stat.KNOWLEDGE,
				knowledgeBaseIndex2 - 1, player2Character);
	}

	@Test
	public void testPlayerResourceViaName() throws BetrayalException {

		// Create a betrayal game
		CreateBetrayal createBetrayal = new CreateBetrayal();
		createBetrayal.setBetrayalId(betrayalId);
		given().
			accept(ContentType.JSON).contentType(ContentType.JSON).
			body(createBetrayal).
		when().
			post("/betrayal-api/betrayalapi/betrayal").
		then().assertThat().
			statusCode(Response.Status.CREATED.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("hauntNumber", equalTo(0)).
			body("$", hasKey("timeCreated")).
			header("Location", equalTo(baseUrl + "/betrayal/" + betrayalId));

		// Add 1 player
		AddPlayer addPlayer = new AddPlayer();
		addPlayer.setName(player1Name);
		addPlayer.setCharacter(player1CharacterString);
		given().
			accept(ContentType.JSON).contentType(ContentType.JSON).
			body(addPlayer).
		when().
			post("/betrayal-api/betrayalapi/betrayal/" + betrayalId + "/players").
		then().assertThat().
			statusCode(Response.Status.CREATED.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player1CharacterString)).
			body("name", equalTo(player1Name)).
			body("isTraitor", equalTo(false)).
			body("isAlive", equalTo(true)).
			body("speed", equalTo(baseSpeedPlayer1)).
			body("might", equalTo(baseMightPlayer1)).
			body("sanity", equalTo(baseSanityPlayer1)).
			body("knowledge", equalTo(baseKnowledgePlayer1)).
			header("Location", equalTo(baseUrl + "/betrayal/" + betrayalId + "/players/character/" + player1CharacterString.replace(" ", "")));

		// Get player via name, verify data
		given().
			accept(ContentType.JSON).
		when().
			get("/betrayal-api/betrayalapi/betrayal/" + betrayalId + "/players/name/" + player1Name).
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player1CharacterString)).
			body("name", equalTo(player1Name)).
			body("isTraitor", equalTo(false)).
			body("isAlive", equalTo(true)).
			body("speed", equalTo(baseSpeedPlayer1)).
			body("might", equalTo(baseMightPlayer1)).
			body("sanity", equalTo(baseSanityPlayer1)).
			body("knowledge", equalTo(baseKnowledgePlayer1));

		// Mark player dead via name
		given().
			accept(ContentType.JSON).
		when().
			post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/name/" + player1Name + "/dead").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player1CharacterString)).
			body("name", equalTo(player1Name)).
			body("isAlive", equalTo(false)).
			body("$", not(hasKey("isTraitor"))).
			body("$", not(hasKey("speed"))).
			body("$", not(hasKey("might"))).
			body("$", not(hasKey("sanity"))).
			body("$", not(hasKey("knowledge"))).
			header("Location", equalTo(baseUrl + "/betrayal/" + betrayalId + "/players/name/" + player1Name));

		// Get player isDead via name, verify data
		given().
			accept(ContentType.JSON).
		when().
			get("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/name/" + player1Name + "/dead").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player1CharacterString)).
			body("name", equalTo(player1Name)).
			body("isAlive", equalTo(false)).
			body("$", not(hasKey("isTraitor"))).
			body("$", not(hasKey("speed"))).
			body("$", not(hasKey("might"))).
			body("$", not(hasKey("sanity"))).
			body("$", not(hasKey("knowledge")));

		// Mark player alive via name
		given().
			accept(ContentType.JSON).
		when().
			post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/name/" + player1Name + "/alive").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player1CharacterString)).
			body("name", equalTo(player1Name)).
			body("isAlive", equalTo(true)).
			body("$", not(hasKey("isTraitor"))).
			body("$", not(hasKey("speed"))).
			body("$", not(hasKey("might"))).
			body("$", not(hasKey("sanity"))).
			body("$", not(hasKey("knowledge"))).
			header("Location", equalTo(baseUrl + "/betrayal/" + betrayalId + "/players/name/" + player1Name));

		// Get player isAlive via name, verify data
		given().
			accept(ContentType.JSON).
		when().
			get("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/name/" + player1Name + "/alive").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player1CharacterString)).
			body("name", equalTo(player1Name)).
			body("isAlive", equalTo(true)).
			body("$", not(hasKey("isTraitor"))).
			body("$", not(hasKey("speed"))).
			body("$", not(hasKey("might"))).
			body("$", not(hasKey("sanity"))).
			body("$", not(hasKey("knowledge")));

		// Mark player traitor via name
		given().
			accept(ContentType.JSON).
		when().
			post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/name/" + player1Name + "/traitor").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player1CharacterString)).
			body("name", equalTo(player1Name)).
			body("isTraitor", equalTo(true)).
			body("$", not(hasKey("isAlive"))).
			body("$", not(hasKey("speed"))).
			body("$", not(hasKey("might"))).
			body("$", not(hasKey("sanity"))).
			body("$", not(hasKey("knowledge"))).
			header("Location", equalTo(baseUrl + "/betrayal/" + betrayalId + "/players/name/" + player1Name));

		// Get player isTraitor via name, verify data
		given().
			accept(ContentType.JSON).
		when().
			get("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/name/" + player1Name + "/traitor").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player1CharacterString)).
			body("name", equalTo(player1Name)).
			body("isTraitor", equalTo(true)).
			body("$", not(hasKey("isAlive"))).
			body("$", not(hasKey("speed"))).
			body("$", not(hasKey("might"))).
			body("$", not(hasKey("sanity"))).
			body("$", not(hasKey("knowledge")));

		// Mark player hero via name
		given().
			accept(ContentType.JSON).
		when().
			post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/name/" + player1Name + "/hero").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player1CharacterString)).
			body("name", equalTo(player1Name)).
			body("isTraitor", equalTo(false)).
			body("$", not(hasKey("isAlive"))).
			body("$", not(hasKey("speed"))).
			body("$", not(hasKey("might"))).
			body("$", not(hasKey("sanity"))).
			body("$", not(hasKey("knowledge"))).
			header("Location", equalTo(baseUrl + "/betrayal/" + betrayalId + "/players/name/" + player1Name));

		// Get player isHero via name, verify data
		given().
			accept(ContentType.JSON).
		when().
			get("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/name/" + player1Name + "/hero").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player1CharacterString)).
			body("name", equalTo(player1Name)).
			body("isTraitor", equalTo(false)).
			body("$", not(hasKey("isAlive"))).
			body("$", not(hasKey("speed"))).
			body("$", not(hasKey("might"))).
			body("$", not(hasKey("sanity"))).
			body("$", not(hasKey("knowledge")));

		// Build stat request objects
		StatRequest incrementRequest = new StatRequest();
		incrementRequest.setStatOperation(StatOperation.INCREMENT);
		StatRequest decrementRequest = new StatRequest();
		decrementRequest.setStatOperation(StatOperation.DECREMENT);
		StatRequest resetRequest = new StatRequest();
		resetRequest.setStatOperation(StatOperation.RESET);

		// Increment speed via name
		given().
			accept(ContentType.JSON).contentType(ContentType.JSON).
			body(incrementRequest).
		when().
			post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/name/" + player1Name + "/speed").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player1CharacterString)).
			body("name", equalTo(player1Name)).
			body("speed", equalTo(plusOneSpeedPlayer1)).
			body("$", not(hasKey("isAlive"))).
			body("$", not(hasKey("isTraitor"))).
			body("$", not(hasKey("might"))).
			body("$", not(hasKey("sanity"))).
			body("$", not(hasKey("knowledge"))).
			header("Location", equalTo(baseUrl + "/betrayal/" + betrayalId + "/players/name/" + player1Name));

		// Reset speed via name
		given().
			accept(ContentType.JSON).contentType(ContentType.JSON).
			body(resetRequest).
		when().
			post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/name/" + player1Name + "/speed").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player1CharacterString)).
			body("name", equalTo(player1Name)).
			body("speed", equalTo(baseSpeedPlayer1)).
			body("$", not(hasKey("isAlive"))).
			body("$", not(hasKey("isTraitor"))).
			body("$", not(hasKey("might"))).
			body("$", not(hasKey("sanity"))).
			body("$", not(hasKey("knowledge"))).
			header("Location", equalTo(baseUrl + "/betrayal/" + betrayalId + "/players/name/" + player1Name));

		// Decrement speed via name
		given().
			accept(ContentType.JSON).contentType(ContentType.JSON).
			body(decrementRequest).
		when().
			post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/name/" + player1Name + "/speed").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player1CharacterString)).
			body("name", equalTo(player1Name)).
			body("speed", equalTo(minusOneSpeedPlayer1)).
			body("$", not(hasKey("isAlive"))).
			body("$", not(hasKey("isTraitor"))).
			body("$", not(hasKey("might"))).
			body("$", not(hasKey("sanity"))).
			body("$", not(hasKey("knowledge"))).
			header("Location", equalTo(baseUrl + "/betrayal/" + betrayalId + "/players/name/" + player1Name));

		// Get speed via name, verify data
		given().
			accept(ContentType.JSON).
		when().
			get("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/name/" + player1Name + "/speed").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player1CharacterString)).
			body("name", equalTo(player1Name)).
			body("speed", equalTo(minusOneSpeedPlayer1)).
			body("$", not(hasKey("isAlive"))).
			body("$", not(hasKey("isTraitor"))).
			body("$", not(hasKey("might"))).
			body("$", not(hasKey("sanity"))).
			body("$", not(hasKey("knowledge")));

		// Decrement speed to death
		for (int i = 0; i < speedBaseIndex1 - 2; i++) {
			given().
				accept(ContentType.JSON).contentType(ContentType.JSON).
				body(decrementRequest).
			when().
				post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/name/" + player1Name + "/speed");
		}
		given().
			accept(ContentType.JSON).contentType(ContentType.JSON).
			body(decrementRequest).
		when().
			post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/name/" + player1Name + "/speed").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player1CharacterString)).
			body("name", equalTo(player1Name)).
			body("speed", equalTo(0)).
			body("isAlive", equalTo(false)).
			body("$", not(hasKey("isTraitor"))).
			body("$", not(hasKey("might"))).
			body("$", not(hasKey("sanity"))).
			body("$", not(hasKey("knowledge"))).
			header("Location", equalTo(baseUrl + "/betrayal/" + betrayalId + "/players/name/" + player1Name));

		// Mark player alive again for next tests
		given().
			accept(ContentType.JSON).
		when().
			post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/name/" + player1Name + "/alive");

		// Increment might via name
		given().
			accept(ContentType.JSON).contentType(ContentType.JSON).
			body(incrementRequest).
		when().
			post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/name/" + player1Name + "/might").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player1CharacterString)).
			body("name", equalTo(player1Name)).
			body("might", equalTo(plusOneMightPlayer1)).
			body("$", not(hasKey("isAlive"))).
			body("$", not(hasKey("isTraitor"))).
			body("$", not(hasKey("speed"))).
			body("$", not(hasKey("sanity"))).
			body("$", not(hasKey("knowledge"))).
			header("Location", equalTo(baseUrl + "/betrayal/" + betrayalId + "/players/name/" + player1Name));

		// Reset might via name
		given().
			accept(ContentType.JSON).contentType(ContentType.JSON).
			body(resetRequest).
		when().
			post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/name/" + player1Name + "/might").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player1CharacterString)).
			body("name", equalTo(player1Name)).
			body("might", equalTo(baseMightPlayer1)).
			body("$", not(hasKey("isAlive"))).
			body("$", not(hasKey("isTraitor"))).
			body("$", not(hasKey("speed"))).
			body("$", not(hasKey("sanity"))).
			body("$", not(hasKey("knowledge"))).
			header("Location", equalTo(baseUrl + "/betrayal/" + betrayalId + "/players/name/" + player1Name));

		// Decrement might via name
		given().
			accept(ContentType.JSON).contentType(ContentType.JSON).
			body(decrementRequest).
		when().
			post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/name/" + player1Name + "/might").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player1CharacterString)).
			body("name", equalTo(player1Name)).
			body("might", equalTo(minusOneMightPlayer1)).
			body("$", not(hasKey("isAlive"))).
			body("$", not(hasKey("isTraitor"))).
			body("$", not(hasKey("speed"))).
			body("$", not(hasKey("sanity"))).
			body("$", not(hasKey("knowledge"))).
			header("Location", equalTo(baseUrl + "/betrayal/" + betrayalId + "/players/name/" + player1Name));

		// Get might via name, verify data
		given().
			accept(ContentType.JSON).
		when().
			get("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/name/" + player1Name + "/might").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player1CharacterString)).
			body("name", equalTo(player1Name)).
			body("might", equalTo(minusOneMightPlayer1)).
			body("$", not(hasKey("isAlive"))).
			body("$", not(hasKey("isTraitor"))).
			body("$", not(hasKey("speed"))).
			body("$", not(hasKey("sanity"))).
			body("$", not(hasKey("knowledge")));

		// Decrement might to death
		for (int i = 0; i < mightBaseIndex1 - 2; i++) {
			given().
				accept(ContentType.JSON).contentType(ContentType.JSON).
				body(decrementRequest).
			when().
				post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/name/" + player1Name + "/might");
		}
		given().
			accept(ContentType.JSON).contentType(ContentType.JSON).
			body(decrementRequest).
		when().
			post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/name/" + player1Name + "/might").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player1CharacterString)).
			body("name", equalTo(player1Name)).
			body("might", equalTo(0)).
			body("isAlive", equalTo(false)).
			body("$", not(hasKey("isTraitor"))).
			body("$", not(hasKey("speed"))).
			body("$", not(hasKey("sanity"))).
			body("$", not(hasKey("knowledge"))).
			header("Location", equalTo(baseUrl + "/betrayal/" + betrayalId + "/players/name/" + player1Name));

		// Mark player alive again for next tests
		given().
			accept(ContentType.JSON).
		when().
			post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/name/" + player1Name + "/alive");

		// Increment sanity via name
		given().
			accept(ContentType.JSON).contentType(ContentType.JSON).
			body(incrementRequest).
		when().
			post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/name/" + player1Name + "/sanity").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player1CharacterString)).
			body("name", equalTo(player1Name)).
			body("sanity", equalTo(plusOneSanityPlayer1)).
			body("$", not(hasKey("isAlive"))).
			body("$", not(hasKey("isTraitor"))).
			body("$", not(hasKey("speed"))).
			body("$", not(hasKey("might"))).
			body("$", not(hasKey("knowledge"))).
			header("Location", equalTo(baseUrl + "/betrayal/" + betrayalId + "/players/name/" + player1Name));

		// Reset sanity via name
		given().
			accept(ContentType.JSON).contentType(ContentType.JSON).
			body(resetRequest).
		when().
			post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/name/" + player1Name + "/sanity").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player1CharacterString)).
			body("name", equalTo(player1Name)).
			body("sanity", equalTo(baseSanityPlayer1)).
			body("$", not(hasKey("isAlive"))).
			body("$", not(hasKey("isTraitor"))).
			body("$", not(hasKey("speed"))).
			body("$", not(hasKey("might"))).
			body("$", not(hasKey("knowledge"))).
			header("Location", equalTo(baseUrl + "/betrayal/" + betrayalId + "/players/name/" + player1Name));

		// Decrement sanity via name
		given().
			accept(ContentType.JSON).contentType(ContentType.JSON).
			body(decrementRequest).
		when().
			post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/name/" + player1Name + "/sanity").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player1CharacterString)).
			body("name", equalTo(player1Name)).
			body("sanity", equalTo(minusOneSanityPlayer1)).
			body("$", not(hasKey("isAlive"))).
			body("$", not(hasKey("isTraitor"))).
			body("$", not(hasKey("speed"))).
			body("$", not(hasKey("might"))).
			body("$", not(hasKey("knowledge"))).
			header("Location", equalTo(baseUrl + "/betrayal/" + betrayalId + "/players/name/" + player1Name));

		// Get sanity via name, verify data
		given().
			accept(ContentType.JSON).
		when().
			get("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/name/" + player1Name + "/sanity").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player1CharacterString)).
			body("name", equalTo(player1Name)).
			body("sanity", equalTo(minusOneSanityPlayer1)).
			body("$", not(hasKey("isAlive"))).
			body("$", not(hasKey("isTraitor"))).
			body("$", not(hasKey("speed"))).
			body("$", not(hasKey("might"))).
			body("$", not(hasKey("knowledge")));

		// Decrement sanity to death
		for (int i = 0; i < sanityBaseIndex1 - 2; i++) {
			given().
				accept(ContentType.JSON).contentType(ContentType.JSON).
				body(decrementRequest).
			when().
				post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/name/" + player1Name + "/sanity");
		}
		given().
			accept(ContentType.JSON).contentType(ContentType.JSON).
			body(decrementRequest).
		when().
			post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/name/" + player1Name + "/sanity").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player1CharacterString)).
			body("name", equalTo(player1Name)).
			body("sanity", equalTo(0)).
			body("isAlive", equalTo(false)).
			body("$", not(hasKey("isTraitor"))).
			body("$", not(hasKey("speed"))).
			body("$", not(hasKey("might"))).
			body("$", not(hasKey("knowledge"))).
			header("Location", equalTo(baseUrl + "/betrayal/" + betrayalId + "/players/name/" + player1Name));

		// Mark player alive again for next tests
		given().
			accept(ContentType.JSON).
		when().
			post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/name/" + player1Name + "/alive");

		// Increment knowledge via name
		given().
			accept(ContentType.JSON).contentType(ContentType.JSON).
			body(incrementRequest).
		when().
			post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/name/" + player1Name + "/knowledge").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player1CharacterString)).
			body("name", equalTo(player1Name)).
			body("knowledge", equalTo(plusOneKnowledgePlayer1)).
			body("$", not(hasKey("isAlive"))).
			body("$", not(hasKey("isTraitor"))).
			body("$", not(hasKey("speed"))).
			body("$", not(hasKey("might"))).
			body("$", not(hasKey("sanity"))).
			header("Location", equalTo(baseUrl + "/betrayal/" + betrayalId + "/players/name/" + player1Name));

		// Reset knowledge via name
		given().
			accept(ContentType.JSON).contentType(ContentType.JSON).
			body(resetRequest).
		when().
			post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/name/" + player1Name + "/knowledge").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player1CharacterString)).
			body("name", equalTo(player1Name)).
			body("knowledge", equalTo(baseKnowledgePlayer1)).
			body("$", not(hasKey("isAlive"))).
			body("$", not(hasKey("isTraitor"))).
			body("$", not(hasKey("speed"))).
			body("$", not(hasKey("might"))).
			body("$", not(hasKey("sanity"))).
			header("Location", equalTo(baseUrl + "/betrayal/" + betrayalId + "/players/name/" + player1Name));

		// Decrement knowledge via name
		given().
			accept(ContentType.JSON).contentType(ContentType.JSON).
			body(decrementRequest).
		when().
			post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/name/" + player1Name + "/knowledge").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player1CharacterString)).
			body("name", equalTo(player1Name)).
			body("knowledge", equalTo(minusOneKnowledgePlayer1)).
			body("$", not(hasKey("isAlive"))).
			body("$", not(hasKey("isTraitor"))).
			body("$", not(hasKey("speed"))).
			body("$", not(hasKey("might"))).
			body("$", not(hasKey("sanity"))).
			header("Location", equalTo(baseUrl + "/betrayal/" + betrayalId + "/players/name/" + player1Name));

		// Get knowledge via name, verify data
		given().
			accept(ContentType.JSON).
		when().
			get("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/name/" + player1Name + "/knowledge").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player1CharacterString)).
			body("name", equalTo(player1Name)).
			body("knowledge", equalTo(minusOneKnowledgePlayer1)).
			body("$", not(hasKey("isAlive"))).
			body("$", not(hasKey("isTraitor"))).
			body("$", not(hasKey("speed"))).
			body("$", not(hasKey("might"))).
			body("$", not(hasKey("sanity")));

		// Decrement knowledge to death
		for (int i = 0; i < knowledgeBaseIndex1 - 2; i++) {
			given().
				accept(ContentType.JSON).contentType(ContentType.JSON).
				body(decrementRequest).
			when().
				post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/name/" + player1Name + "/knowledge");
		}
		given().
			accept(ContentType.JSON).contentType(ContentType.JSON).
			body(decrementRequest).
		when().
			post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/name/" + player1Name + "/knowledge").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player1CharacterString)).
			body("name", equalTo(player1Name)).
			body("knowledge", equalTo(0)).
			body("isAlive", equalTo(false)).
			body("$", not(hasKey("isTraitor"))).
			body("$", not(hasKey("speed"))).
			body("$", not(hasKey("might"))).
			body("$", not(hasKey("sanity"))).
			header("Location", equalTo(baseUrl + "/betrayal/" + betrayalId + "/players/name/" + player1Name));

		// Mark player alive again for next tests
		given().
			accept(ContentType.JSON).
		when().
			post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/name/" + player1Name + "/alive");

		// Delete player via name
		given().when().
			delete("/betrayal-api/betrayalapi/betrayal/" + betrayalId + "/players/name/" + player1Name).
		then().assertThat().
			statusCode(Response.Status.NO_CONTENT.getStatusCode()).
			body(Matchers.isEmptyString());

		// Get player, make sure they're gone
		given().
			accept(ContentType.JSON).
		when().
			get("/betrayal-api/betrayalapi/betrayal/" + betrayalId + "/players/name" + player1Name).
		then().assertThat().
			statusCode(Response.Status.NOT_FOUND.getStatusCode());

		// Delete the betrayal game
		given().when().
			delete("/betrayal-api/betrayalapi/betrayal/" + betrayalId).
		then().assertThat().
			statusCode(Response.Status.NO_CONTENT.getStatusCode()).
			body(Matchers.isEmptyString());
	}

	@Test
	public void testPlayerResourceViaCharacter() throws BetrayalException {

		// Create a betrayal game
		CreateBetrayal createBetrayal = new CreateBetrayal();
		createBetrayal.setBetrayalId(betrayalId);
		given().
			accept(ContentType.JSON).contentType(ContentType.JSON).
			body(createBetrayal).
		when().
			post("/betrayal-api/betrayalapi/betrayal").
		then().assertThat().
			statusCode(Response.Status.CREATED.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("hauntNumber", equalTo(0)).
			body("$", hasKey("timeCreated")).
			header("Location", equalTo(baseUrl + "/betrayal/" + betrayalId));

		// Add 1 player
		AddPlayer addPlayer = new AddPlayer();
		addPlayer.setName(player2Name);
		addPlayer.setCharacter(player2CharacterString);
		given().
			accept(ContentType.JSON).contentType(ContentType.JSON).
			body(addPlayer).
		when().
			post("/betrayal-api/betrayalapi/betrayal/" + betrayalId + "/players").
		then().assertThat().
			statusCode(Response.Status.CREATED.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player2CharacterString)).
			body("name", equalTo(player2Name)).
			body("isTraitor", equalTo(false)).
			body("isAlive", equalTo(true)).
			body("speed", equalTo(baseSpeedPlayer2)).
			body("might", equalTo(baseMightPlayer2)).
			body("sanity", equalTo(baseSanityPlayer2)).
			body("knowledge", equalTo(baseKnowledgePlayer2)).
			header("Location", equalTo(baseUrl + "/betrayal/" + betrayalId + "/players/character/" + player2CharacterString.replace(" ", "")));

		// Get player via character, verify data
		given().
			accept(ContentType.JSON).
		when().
			get("/betrayal-api/betrayalapi/betrayal/" + betrayalId + "/players/character/" + player2CharacterString).
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player2CharacterString)).
			body("name", equalTo(player2Name)).
			body("isTraitor", equalTo(false)).
			body("isAlive", equalTo(true)).
			body("speed", equalTo(baseSpeedPlayer2)).
			body("might", equalTo(baseMightPlayer2)).
			body("sanity", equalTo(baseSanityPlayer2)).
			body("knowledge", equalTo(baseKnowledgePlayer2));

		// Mark player dead via character
		given().
			accept(ContentType.JSON).
		when().
			post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/character/" + player2CharacterString + "/dead").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player2CharacterString)).
			body("name", equalTo(player2Name)).
			body("isAlive", equalTo(false)).
			body("$", not(hasKey("isTraitor"))).
			body("$", not(hasKey("speed"))).
			body("$", not(hasKey("might"))).
			body("$", not(hasKey("sanity"))).
			body("$", not(hasKey("knowledge"))).
			header("Location", equalTo(baseUrl + "/betrayal/" + betrayalId + "/players/character/" + player2CharacterString.replace(" ", "")));

		// Get player isDead via character, verify data
		given().
			accept(ContentType.JSON).
		when().
			get("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/character/" + player2CharacterString + "/dead").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player2CharacterString)).
			body("name", equalTo(player2Name)).
			body("isAlive", equalTo(false)).
			body("$", not(hasKey("isTraitor"))).
			body("$", not(hasKey("speed"))).
			body("$", not(hasKey("might"))).
			body("$", not(hasKey("sanity"))).
			body("$", not(hasKey("knowledge")));

		// Mark player alive via character
		given().
			accept(ContentType.JSON).
		when().
			post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/character/" + player2CharacterString + "/alive").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player2CharacterString)).
			body("name", equalTo(player2Name)).
			body("isAlive", equalTo(true)).
			body("$", not(hasKey("isTraitor"))).
			body("$", not(hasKey("speed"))).
			body("$", not(hasKey("might"))).
			body("$", not(hasKey("sanity"))).
			body("$", not(hasKey("knowledge"))).
			header("Location", equalTo(baseUrl + "/betrayal/" + betrayalId + "/players/character/" + player2CharacterString.replace(" ", "")));

		// Get player isAlive via character, verify data
		given().
			accept(ContentType.JSON).
		when().
			get("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/character/" + player2CharacterString + "/alive").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player2CharacterString)).
			body("name", equalTo(player2Name)).
			body("isAlive", equalTo(true)).
			body("$", not(hasKey("isTraitor"))).
			body("$", not(hasKey("speed"))).
			body("$", not(hasKey("might"))).
			body("$", not(hasKey("sanity"))).
			body("$", not(hasKey("knowledge")));

		// Mark player traitor via character
		given().
			accept(ContentType.JSON).
		when().
			post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/character/" + player2CharacterString + "/traitor").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player2CharacterString)).
			body("name", equalTo(player2Name)).
			body("isTraitor", equalTo(true)).
			body("$", not(hasKey("isAlive"))).
			body("$", not(hasKey("speed"))).
			body("$", not(hasKey("might"))).
			body("$", not(hasKey("sanity"))).
			body("$", not(hasKey("knowledge"))).
			header("Location", equalTo(baseUrl + "/betrayal/" + betrayalId + "/players/character/" + player2CharacterString.replace(" ", "")));

		// Get player isTraitor via character, verify data
		given().
			accept(ContentType.JSON).
		when().
			get("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/character/" + player2CharacterString + "/traitor").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player2CharacterString)).
			body("name", equalTo(player2Name)).
			body("isTraitor", equalTo(true)).
			body("$", not(hasKey("isAlive"))).
			body("$", not(hasKey("speed"))).
			body("$", not(hasKey("might"))).
			body("$", not(hasKey("sanity"))).
			body("$", not(hasKey("knowledge")));

		// Mark player hero via character
		given().
			accept(ContentType.JSON).
		when().
			post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/character/" + player2CharacterString + "/hero").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player2CharacterString)).
			body("name", equalTo(player2Name)).
			body("isTraitor", equalTo(false)).
			body("$", not(hasKey("isAlive"))).
			body("$", not(hasKey("speed"))).
			body("$", not(hasKey("might"))).
			body("$", not(hasKey("sanity"))).
			body("$", not(hasKey("knowledge"))).
			header("Location", equalTo(baseUrl + "/betrayal/" + betrayalId + "/players/character/" + player2CharacterString.replace(" ", "")));

		// Get player isHero via character, verify data
		given().
			accept(ContentType.JSON).
		when().
			get("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/character/" + player2CharacterString + "/hero").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player2CharacterString)).
			body("name", equalTo(player2Name)).
			body("isTraitor", equalTo(false)).
			body("$", not(hasKey("isAlive"))).
			body("$", not(hasKey("speed"))).
			body("$", not(hasKey("might"))).
			body("$", not(hasKey("sanity"))).
			body("$", not(hasKey("knowledge")));

		// Build stat request objects
		StatRequest incrementRequest = new StatRequest();
		incrementRequest.setStatOperation(StatOperation.INCREMENT);
		StatRequest decrementRequest = new StatRequest();
		decrementRequest.setStatOperation(StatOperation.DECREMENT);
		StatRequest resetRequest = new StatRequest();
		resetRequest.setStatOperation(StatOperation.RESET);

		// Increment speed via character
		given().
			accept(ContentType.JSON).contentType(ContentType.JSON).
			body(incrementRequest).
		when().
			post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/character/" + player2CharacterString + "/speed").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player2CharacterString)).
			body("name", equalTo(player2Name)).
			body("speed", equalTo(plusOneSpeedPlayer2)).
			body("$", not(hasKey("isAlive"))).
			body("$", not(hasKey("isTraitor"))).
			body("$", not(hasKey("might"))).
			body("$", not(hasKey("sanity"))).
			body("$", not(hasKey("knowledge"))).
			header("Location", equalTo(baseUrl + "/betrayal/" + betrayalId + "/players/character/" + player2CharacterString.replace(" ", "")));

		// Reset speed via character
		given().
			accept(ContentType.JSON).contentType(ContentType.JSON).
			body(resetRequest).
		when().
			post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/character/" + player2CharacterString + "/speed").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player2CharacterString)).
			body("name", equalTo(player2Name)).
			body("speed", equalTo(baseSpeedPlayer2)).
			body("$", not(hasKey("isAlive"))).
			body("$", not(hasKey("isTraitor"))).
			body("$", not(hasKey("might"))).
			body("$", not(hasKey("sanity"))).
			body("$", not(hasKey("knowledge"))).
			header("Location", equalTo(baseUrl + "/betrayal/" + betrayalId + "/players/character/" + player2CharacterString.replace(" ", "")));

		// Decrement speed via character
		given().
			accept(ContentType.JSON).contentType(ContentType.JSON).
			body(decrementRequest).
		when().
			post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/character/" + player2CharacterString + "/speed").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player2CharacterString)).
			body("name", equalTo(player2Name)).
			body("speed", equalTo(minusOneSpeedPlayer2)).
			body("$", not(hasKey("isAlive"))).
			body("$", not(hasKey("isTraitor"))).
			body("$", not(hasKey("might"))).
			body("$", not(hasKey("sanity"))).
			body("$", not(hasKey("knowledge"))).
			header("Location", equalTo(baseUrl + "/betrayal/" + betrayalId + "/players/character/" + player2CharacterString.replace(" ", "")));

		// Get speed via character, verify data
		given().
			accept(ContentType.JSON).
		when().
			get("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/character/" + player2CharacterString + "/speed").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player2CharacterString)).
			body("name", equalTo(player2Name)).
			body("speed", equalTo(minusOneSpeedPlayer2)).
			body("$", not(hasKey("isAlive"))).
			body("$", not(hasKey("isTraitor"))).
			body("$", not(hasKey("might"))).
			body("$", not(hasKey("sanity"))).
			body("$", not(hasKey("knowledge")));

		// Decrement speed to death
		for (int i = 0; i < speedBaseIndex2 - 2; i++) {
			given().
				accept(ContentType.JSON).contentType(ContentType.JSON).
				body(decrementRequest).
			when().
				post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/character/" + player2CharacterString + "/speed");
		}
		given().
			accept(ContentType.JSON).contentType(ContentType.JSON).
			body(decrementRequest).
		when().
			post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/character/" + player2CharacterString + "/speed").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player2CharacterString)).
			body("name", equalTo(player2Name)).
			body("speed", equalTo(0)).
			body("isAlive", equalTo(false)).
			body("$", not(hasKey("isTraitor"))).
			body("$", not(hasKey("might"))).
			body("$", not(hasKey("sanity"))).
			body("$", not(hasKey("knowledge"))).
			header("Location", equalTo(baseUrl + "/betrayal/" + betrayalId + "/players/character/" + player2CharacterString.replace(" ", "")));

		// Mark player alive again for next tests
		given().
			accept(ContentType.JSON).
		when().
			post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/character/" + player2CharacterString + "/alive");

		// Increment might via character
		given().
			accept(ContentType.JSON).contentType(ContentType.JSON).
			body(incrementRequest).
		when().
			post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/character/" + player2CharacterString + "/might").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player2CharacterString)).
			body("name", equalTo(player2Name)).
			body("might", equalTo(plusOneMightPlayer2)).
			body("$", not(hasKey("isAlive"))).
			body("$", not(hasKey("isTraitor"))).
			body("$", not(hasKey("speed"))).
			body("$", not(hasKey("sanity"))).
			body("$", not(hasKey("knowledge"))).
			header("Location", equalTo(baseUrl + "/betrayal/" + betrayalId + "/players/character/" + player2CharacterString.replace(" ", "")));

		// Reset might via character
		given().
			accept(ContentType.JSON).contentType(ContentType.JSON).
			body(resetRequest).
		when().
			post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/character/" + player2CharacterString + "/might").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player2CharacterString)).
			body("name", equalTo(player2Name)).
			body("might", equalTo(baseMightPlayer2)).
			body("$", not(hasKey("isAlive"))).
			body("$", not(hasKey("isTraitor"))).
			body("$", not(hasKey("speed"))).
			body("$", not(hasKey("sanity"))).
			body("$", not(hasKey("knowledge"))).
			header("Location", equalTo(baseUrl + "/betrayal/" + betrayalId + "/players/character/" + player2CharacterString.replace(" ", "")));

		// Decrement might via character
		given().
			accept(ContentType.JSON).contentType(ContentType.JSON).
			body(decrementRequest).
		when().
			post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/character/" + player2CharacterString + "/might").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player2CharacterString)).
			body("name", equalTo(player2Name)).
			body("might", equalTo(minusOneMightPlayer2)).
			body("$", not(hasKey("isAlive"))).
			body("$", not(hasKey("isTraitor"))).
			body("$", not(hasKey("speed"))).
			body("$", not(hasKey("sanity"))).
			body("$", not(hasKey("knowledge"))).
			header("Location", equalTo(baseUrl + "/betrayal/" + betrayalId + "/players/character/" + player2CharacterString.replace(" ", "")));

		// Get might via character, verify data
		given().
			accept(ContentType.JSON).
		when().
			get("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/character/" + player2CharacterString + "/might").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player2CharacterString)).
			body("name", equalTo(player2Name)).
			body("might", equalTo(minusOneMightPlayer2)).
			body("$", not(hasKey("isAlive"))).
			body("$", not(hasKey("isTraitor"))).
			body("$", not(hasKey("speed"))).
			body("$", not(hasKey("sanity"))).
			body("$", not(hasKey("knowledge")));

		// Decrement might to death
		for (int i = 0; i < mightBaseIndex2 - 2; i++) {
			given().
				accept(ContentType.JSON).contentType(ContentType.JSON).
				body(decrementRequest).
			when().
				post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/character/" + player2CharacterString + "/might");
		}
		given().
			accept(ContentType.JSON).contentType(ContentType.JSON).
			body(decrementRequest).
		when().
			post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/character/" + player2CharacterString + "/might").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player2CharacterString)).
			body("name", equalTo(player2Name)).
			body("might", equalTo(0)).
			body("isAlive", equalTo(false)).
			body("$", not(hasKey("isTraitor"))).
			body("$", not(hasKey("speed"))).
			body("$", not(hasKey("sanity"))).
			body("$", not(hasKey("knowledge"))).
			header("Location", equalTo(baseUrl + "/betrayal/" + betrayalId + "/players/character/" + player2CharacterString.replace(" ", "")));

		// Mark player alive again for next tests
		given().
			accept(ContentType.JSON).
		when().
			post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/character/" + player2CharacterString + "/alive");

		// Increment sanity via character
		given().
			accept(ContentType.JSON).contentType(ContentType.JSON).
			body(incrementRequest).
		when().
			post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/character/" + player2CharacterString + "/sanity").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player2CharacterString)).
			body("name", equalTo(player2Name)).
			body("sanity", equalTo(plusOneSanityPlayer2)).
			body("$", not(hasKey("isAlive"))).
			body("$", not(hasKey("isTraitor"))).
			body("$", not(hasKey("speed"))).
			body("$", not(hasKey("might"))).
			body("$", not(hasKey("knowledge"))).
			header("Location", equalTo(baseUrl + "/betrayal/" + betrayalId + "/players/character/" + player2CharacterString.replace(" ", "")));

		// Reset sanity via character
		given().
			accept(ContentType.JSON).contentType(ContentType.JSON).
			body(resetRequest).
		when().
			post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/character/" + player2CharacterString + "/sanity").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player2CharacterString)).
			body("name", equalTo(player2Name)).
			body("sanity", equalTo(baseSanityPlayer2)).
			body("$", not(hasKey("isAlive"))).
			body("$", not(hasKey("isTraitor"))).
			body("$", not(hasKey("speed"))).
			body("$", not(hasKey("might"))).
			body("$", not(hasKey("knowledge"))).
			header("Location", equalTo(baseUrl + "/betrayal/" + betrayalId + "/players/character/" + player2CharacterString.replace(" ", "")));

		// Decrement sanity via character
		given().
			accept(ContentType.JSON).contentType(ContentType.JSON).
			body(decrementRequest).
		when().
			post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/character/" + player2CharacterString + "/sanity").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player2CharacterString)).
			body("name", equalTo(player2Name)).
			body("sanity", equalTo(minusOneSanityPlayer2)).
			body("$", not(hasKey("isAlive"))).
			body("$", not(hasKey("isTraitor"))).
			body("$", not(hasKey("speed"))).
			body("$", not(hasKey("might"))).
			body("$", not(hasKey("knowledge"))).
			header("Location", equalTo(baseUrl + "/betrayal/" + betrayalId + "/players/character/" + player2CharacterString.replace(" ", "")));

		// Get sanity via character, verify data
		given().
			accept(ContentType.JSON).
		when().
			get("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/character/" + player2CharacterString + "/sanity").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player2CharacterString)).
			body("name", equalTo(player2Name)).
			body("sanity", equalTo(minusOneSanityPlayer2)).
			body("$", not(hasKey("isAlive"))).
			body("$", not(hasKey("isTraitor"))).
			body("$", not(hasKey("speed"))).
			body("$", not(hasKey("might"))).
			body("$", not(hasKey("knowledge")));

		// Decrement sanity to death
		for (int i = 0; i < sanityBaseIndex2 - 2; i++) {
			given().
				accept(ContentType.JSON).contentType(ContentType.JSON).
				body(decrementRequest).
			when().
				post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/character/" + player2CharacterString + "/sanity");
		}
		given().
			accept(ContentType.JSON).contentType(ContentType.JSON).
			body(decrementRequest).
		when().
			post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/character/" + player2CharacterString + "/sanity").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player2CharacterString)).
			body("name", equalTo(player2Name)).
			body("sanity", equalTo(0)).
			body("isAlive", equalTo(false)).
			body("$", not(hasKey("isTraitor"))).
			body("$", not(hasKey("speed"))).
			body("$", not(hasKey("might"))).
			body("$", not(hasKey("knowledge"))).
			header("Location", equalTo(baseUrl + "/betrayal/" + betrayalId + "/players/character/" + player2CharacterString.replace(" ", "")));

		// Mark player alive again for next tests
		given().
			accept(ContentType.JSON).
		when().
			post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/character/" + player2CharacterString + "/alive");

		// Increment knowledge via character
		given().
			accept(ContentType.JSON).contentType(ContentType.JSON).
			body(incrementRequest).
		when().
			post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/character/" + player2CharacterString + "/knowledge").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player2CharacterString)).
			body("name", equalTo(player2Name)).
			body("knowledge", equalTo(plusOneKnowledgePlayer2)).
			body("$", not(hasKey("isAlive"))).
			body("$", not(hasKey("isTraitor"))).
			body("$", not(hasKey("speed"))).
			body("$", not(hasKey("might"))).
			body("$", not(hasKey("sanity"))).
			header("Location", equalTo(baseUrl + "/betrayal/" + betrayalId + "/players/character/" + player2CharacterString.replace(" ", "")));

		// Reset knowledge via character
		given().
			accept(ContentType.JSON).contentType(ContentType.JSON).
			body(resetRequest).
		when().
			post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/character/" + player2CharacterString + "/knowledge").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player2CharacterString)).
			body("name", equalTo(player2Name)).
			body("knowledge", equalTo(baseKnowledgePlayer2)).
			body("$", not(hasKey("isAlive"))).
			body("$", not(hasKey("isTraitor"))).
			body("$", not(hasKey("speed"))).
			body("$", not(hasKey("might"))).
			body("$", not(hasKey("sanity"))).
			header("Location", equalTo(baseUrl + "/betrayal/" + betrayalId + "/players/character/" + player2CharacterString.replace(" ", "")));

		// Decrement knowledge via character
		given().
			accept(ContentType.JSON).contentType(ContentType.JSON).
			body(decrementRequest).
		when().
			post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/character/" + player2CharacterString + "/knowledge").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player2CharacterString)).
			body("name", equalTo(player2Name)).
			body("knowledge", equalTo(minusOneKnowledgePlayer2)).
			body("$", not(hasKey("isAlive"))).
			body("$", not(hasKey("isTraitor"))).
			body("$", not(hasKey("speed"))).
			body("$", not(hasKey("might"))).
			body("$", not(hasKey("sanity"))).
			header("Location", equalTo(baseUrl + "/betrayal/" + betrayalId + "/players/character/" + player2CharacterString.replace(" ", "")));

		// Get knowledge via character, verify data
		given().
			accept(ContentType.JSON).
		when().
			get("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/character/" + player2CharacterString + "/knowledge").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player2CharacterString)).
			body("name", equalTo(player2Name)).
			body("knowledge", equalTo(minusOneKnowledgePlayer2)).
			body("$", not(hasKey("isAlive"))).
			body("$", not(hasKey("isTraitor"))).
			body("$", not(hasKey("speed"))).
			body("$", not(hasKey("might"))).
			body("$", not(hasKey("sanity")));

		// Decrement knowledge to death
		for (int i = 0; i < knowledgeBaseIndex2 - 2; i++) {
			given().
				accept(ContentType.JSON).contentType(ContentType.JSON).
				body(decrementRequest).
			when().
				post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/character/" + player2CharacterString + "/knowledge");
		}
		given().
			accept(ContentType.JSON).contentType(ContentType.JSON).
			body(decrementRequest).
		when().
			post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/character/" + player2CharacterString + "/knowledge").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("character", equalTo(player2CharacterString)).
			body("name", equalTo(player2Name)).
			body("knowledge", equalTo(0)).
			body("isAlive", equalTo(false)).
			body("$", not(hasKey("isTraitor"))).
			body("$", not(hasKey("speed"))).
			body("$", not(hasKey("might"))).
			body("$", not(hasKey("sanity"))).
			header("Location", equalTo(baseUrl + "/betrayal/" + betrayalId + "/players/character/" + player2CharacterString.replace(" ", "")));

		// Mark player alive again for next tests
		given().
			accept(ContentType.JSON).
		when().
			post("/betrayal-api/betrayalapi/betrayal/" + betrayalId  + "/players/character/" + player2CharacterString + "/alive");

		// Delete player via character
		given().when().
			delete("/betrayal-api/betrayalapi/betrayal/" + betrayalId + "/players/character/" + player2CharacterString).
		then().assertThat().
			statusCode(Response.Status.NO_CONTENT.getStatusCode()).
			body(Matchers.isEmptyString());

		// Get player, make sure they're gone
		given().
			accept(ContentType.JSON).
		when().
			get("/betrayal-api/betrayalapi/betrayal/" + betrayalId + "/players/character" + player2CharacterString).
		then().assertThat().
			statusCode(Response.Status.NOT_FOUND.getStatusCode());

		// Delete the betrayal game
		given().when().
			delete("/betrayal-api/betrayalapi/betrayal/" + betrayalId).
		then().assertThat().
			statusCode(Response.Status.NO_CONTENT.getStatusCode()).
			body(Matchers.isEmptyString());
	}

//	@Test
//	public void testPlayerResourceMultiplePlayers() {
//		// Add multiple players
//		// Set some things
//		// Test all the getAll methods
//		// Delete betrayal game
//	}
}