package com.github.aawkall.betrayalapi.resource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;

import javax.ws.rs.core.Response;

import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import com.github.aawkall.betrayalapi.entity.request.CreateBetrayal;
import com.github.aawkall.betrayalapi.entity.request.SetHauntNumber;

public class BetrayalResourceTest {

	public static final String baseUrl = "http://localhost:8080/betrayal-api/betrayalapi";
	public static final String betrayalId = "TestBetrayalId";

	@Test
	public void testBetrayalResource() {

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

		// Get the betrayal game, verify data
		given().
			accept(ContentType.JSON).
		when().
			get("/betrayal-api/betrayalapi/betrayal/" + betrayalId).
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("hauntNumber", equalTo(0)).
			body("$", hasKey("timeCreated"));

		// Set the haunt number
		SetHauntNumber setHauntNumber = new SetHauntNumber();
		setHauntNumber.setHauntNumber(15);
		given().
			accept(ContentType.JSON).contentType(ContentType.JSON).
			body(setHauntNumber).
		when().
			post("/betrayal-api/betrayalapi/betrayal/" + betrayalId + "/hauntnumber").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("hauntNumber", equalTo(15)).
			body("$", not(hasKey("timeCreated"))).
			header("Location", equalTo(baseUrl + "/betrayal/" + betrayalId));

		// Get the haunt number, verify data only includes haunt number and id
		given().
			accept(ContentType.JSON).
		when().
			get("/betrayal-api/betrayalapi/betrayal/" + betrayalId + "/hauntnumber").
		then().assertThat().
			statusCode(Response.Status.OK.getStatusCode()).
			body("betrayalId", equalTo(betrayalId)).
			body("hauntNumber", equalTo(15)).
			body("$", not(hasKey("timeCreated")));

		// Delete the betrayal game
		given().when().
			delete("/betrayal-api/betrayalapi/betrayal/" + betrayalId).
		then().assertThat().
			statusCode(Response.Status.NO_CONTENT.getStatusCode()).
			body(Matchers.isEmptyString());

		// Get the game, make sure it's gone
		given().
			accept(ContentType.JSON).
		when().
			get("/betrayal-api/betrayalapi/betrayal/" + betrayalId).
		then().assertThat().
			statusCode(Response.Status.NOT_FOUND.getStatusCode());
	}
}