package com.github.aawkall.betrayalapi.resource;

import java.net.URI;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.github.aawkall.betrayalapi.entity.db.Player;
import com.github.aawkall.betrayalapi.entity.db.Player.Character;
import com.github.aawkall.betrayalapi.entity.request.AddPlayer;
import com.github.aawkall.betrayalapi.entity.request.StatRequest;
import com.github.aawkall.betrayalapi.entity.response.PlayerResponse;
import com.github.aawkall.betrayalapi.exception.BetrayalException;
import com.github.aawkall.betrayalapi.service.BetrayalService;
import com.github.aawkall.betrayalapi.util.BetrayalConst.Stat;
import com.github.aawkall.betrayalapi.util.BetrayalUtils;

import ma.glasnost.orika.MapperFacade;

@Path("/betrayalapi/betrayal/{betrayalId}/players")
@Produces(MediaType.APPLICATION_JSON)
public class PlayerResource {

	private final BetrayalService betrayalService;
	private final MapperFacade mapperFacade;

	@Context
	private UriInfo uriInfo;

	@Inject
	public PlayerResource(final BetrayalService betrayalService, final MapperFacade mapperFacade) {
		this.betrayalService = betrayalService;
		this.mapperFacade = mapperFacade;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addPlayer(@PathParam("betrayalId") final String betrayalId, final AddPlayer addPlayer) throws Exception {
		Player player = betrayalService.addPlayer(betrayalId, addPlayer.getCharacter(), addPlayer.getName());

		final URI playerUri = UriBuilder.fromUri(uriInfo.getRequestUri())
				.path("/character/{character}")
				.resolveTemplate("character", player.getCharacter().toString().replace(" ", ""))
				.build();
		PlayerResponse response = mapperFacade.map(player, PlayerResponse.class);
		return Response.created(playerUri).entity(response).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllPlayers(@PathParam("betrayalId") final String betrayalId) throws Exception {
		List<Player> getAllPlayers = betrayalService.getAllPlayers(betrayalId);
		List<PlayerResponse> allPlayers = BetrayalUtils.mapPlayerList(getAllPlayers, mapperFacade);
		return Response.ok(allPlayers).build();
	}

	@GET
	@Path("/traitors")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllTraitors(@PathParam("betrayalId") final String betrayalId) throws Exception {
		List<Player> getAllTraitors = betrayalService.getAllTraitors(betrayalId);
		List<PlayerResponse> allTraitors = BetrayalUtils.mapPlayerList(getAllTraitors, mapperFacade);
		return Response.ok(allTraitors).build();
	}

	@GET
	@Path("/heroes")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllHeroes(@PathParam("betrayalId") final String betrayalId) throws Exception {
		List<Player> getAllHeroes = betrayalService.getAllHeroes(betrayalId);
		List<PlayerResponse> allHeroes = BetrayalUtils.mapPlayerList(getAllHeroes, mapperFacade);
		return Response.ok(allHeroes).build();
	}

	@GET
	@Path("/alive")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllAlive(@PathParam("betrayalId") final String betrayalId) throws Exception {
		List<Player> getAllAlive = betrayalService.getAllAlive(betrayalId);
		List<PlayerResponse> allAlive = BetrayalUtils.mapPlayerList(getAllAlive, mapperFacade);
		return Response.ok(allAlive).build();
	}

	@GET
	@Path("/dead")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllDead(@PathParam("betrayalId") final String betrayalId) throws Exception {
		List<Player> getAllDead = betrayalService.getAllDead(betrayalId);
		List<PlayerResponse> allDead = BetrayalUtils.mapPlayerList(getAllDead, mapperFacade);
		return Response.ok(allDead).build();
	}

	@GET
	@Path("/character/{character}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPlayerByCharacter(@PathParam("betrayalId") final String betrayalId, @PathParam("character") final String character) throws Exception {
		Character playerCharacter = Character.fromString(character);
		Player getPlayer = betrayalService.getPlayer(betrayalId, playerCharacter);
		PlayerResponse response = mapperFacade.map(getPlayer, PlayerResponse.class);
		return Response.ok(response).build();
	}

	@GET
	@Path("/name/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPlayerByName(@PathParam("betrayalId") final String betrayalId, @PathParam("name") final String name) throws Exception {
		Player getPlayer = betrayalService.getPlayer(betrayalId, name);
		PlayerResponse response = mapperFacade.map(getPlayer, PlayerResponse.class);
		return Response.ok(response).build();
	}

	@POST
	@Path("/character/{character}/dead")
	@Produces(MediaType.APPLICATION_JSON)
	public Response markAsDeadByCharacter(@PathParam("betrayalId") final String betrayalId, @PathParam("character") final String character) throws Exception {
		Character playerCharacter = Character.fromString(character);
		Player deadPlayer = betrayalService.markAsDead(betrayalId, playerCharacter);

		// Strip uri of the last segment and return location header pointing to player resource via character
		final URI playerUri = BetrayalUtils.getRequestUriWithoutLastSegment(uriInfo);

		// Build response, filter to just send back ids and isAlive status
		PlayerResponse response = mapperFacade.map(deadPlayer, PlayerResponse.class);
		response.initFieldFiltering();
		response.setIncludeIsAlive(true);

		return Response.ok(response).location(playerUri).build();
	}

	@GET
	@Path("/character/{character}/dead")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDeadStatusByCharacter(@PathParam("betrayalId") final String betrayalId, @PathParam("character") final String character) throws Exception {
		Character playerCharacter = Character.fromString(character);
		Player getPlayer = betrayalService.getPlayer(betrayalId, playerCharacter);

		// Filter response to just send back ids and isAlive status
		PlayerResponse response = mapperFacade.map(getPlayer, PlayerResponse.class);
		response.initFieldFiltering();
		response.setIncludeIsAlive(true);

		return Response.ok(response).build();
	}

	@POST
	@Path("/name/{name}/dead")
	@Produces(MediaType.APPLICATION_JSON)
	public Response markAsDeadByName(@PathParam("betrayalId") final String betrayalId, @PathParam("name") final String name) throws Exception {
		Player deadPlayer = betrayalService.markAsDead(betrayalId, name);

		// Strip uri of the last segment and return location header pointing to player resource via name
		final URI playerUri = BetrayalUtils.getRequestUriWithoutLastSegment(uriInfo);

		// Build response, filter to just send back ids and isAlive status
		PlayerResponse response = mapperFacade.map(deadPlayer, PlayerResponse.class);
		response.initFieldFiltering();
		response.setIncludeIsAlive(true);

		return Response.ok(response).location(playerUri).build();
	}

	@GET
	@Path("/name/{name}/dead")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDeadStatusByName(@PathParam("betrayalId") final String betrayalId, @PathParam("name") final String name) throws Exception {
		Player getPlayer = betrayalService.getPlayer(betrayalId, name);

		// Filter response to just send back ids and isAlive status
		PlayerResponse response = mapperFacade.map(getPlayer, PlayerResponse.class);
		response.initFieldFiltering();
		response.setIncludeIsAlive(true);

		return Response.ok(response).build();
	}

	@POST
	@Path("/character/{character}/alive")
	@Produces(MediaType.APPLICATION_JSON)
	public Response markAsAliveByCharacter(@PathParam("betrayalId") final String betrayalId, @PathParam("character") final String character) throws Exception {
		Character playerCharacter = Character.fromString(character);
		Player alivePlayer = betrayalService.markAsAlive(betrayalId, playerCharacter);

		// Strip uri of the last segment and return location header pointing to player resource via character
		final URI playerUri = BetrayalUtils.getRequestUriWithoutLastSegment(uriInfo);

		// Build response, filter to just send back ids and isAlive status
		PlayerResponse response = mapperFacade.map(alivePlayer, PlayerResponse.class);
		response.initFieldFiltering();
		response.setIncludeIsAlive(true);

		return Response.ok(response).location(playerUri).build();
	}

	@GET
	@Path("/character/{character}/alive")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAliveStatusByCharacter(@PathParam("betrayalId") final String betrayalId, @PathParam("character") final String character) throws Exception {
		Character playerCharacter = Character.fromString(character);
		Player getPlayer = betrayalService.getPlayer(betrayalId, playerCharacter);

		// Filter response to just send back ids and isAlive status
		PlayerResponse response = mapperFacade.map(getPlayer, PlayerResponse.class);
		response.initFieldFiltering();
		response.setIncludeIsAlive(true);

		return Response.ok(response).build();
	}

	@POST
	@Path("/name/{name}/alive")
	@Produces(MediaType.APPLICATION_JSON)
	public Response markAsAliveByName(@PathParam("betrayalId") final String betrayalId, @PathParam("name") final String name) throws Exception {
		Player alivePlayer = betrayalService.markAsAlive(betrayalId, name);

		// Strip uri of the last segment and return location header pointing to player resource via name
		final URI playerUri = BetrayalUtils.getRequestUriWithoutLastSegment(uriInfo);

		// Build response, filter to just send back ids and isAlive status
		PlayerResponse response = mapperFacade.map(alivePlayer, PlayerResponse.class);
		response.initFieldFiltering();
		response.setIncludeIsAlive(true);

		return Response.ok(response).location(playerUri).build();
	}

	@GET
	@Path("/name/{name}/alive")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAliveStatusByName(@PathParam("betrayalId") final String betrayalId, @PathParam("name") final String name) throws Exception {
		Player getPlayer = betrayalService.getPlayer(betrayalId, name);

		// Filter response to just send back ids and isAlive status
		PlayerResponse response = mapperFacade.map(getPlayer, PlayerResponse.class);
		response.initFieldFiltering();
		response.setIncludeIsAlive(true);

		return Response.ok(response).build();
	}

	@POST
	@Path("/character/{character}/traitor")
	@Produces(MediaType.APPLICATION_JSON)
	public Response markAsTraitorByCharacter(@PathParam("betrayalId") final String betrayalId, @PathParam("character") final String character) throws Exception {
		Character playerCharacter = Character.fromString(character);
		Player traitor = betrayalService.markAsTraitor(betrayalId, playerCharacter);

		// Strip uri of the last segment and return location header pointing to player resource via character
		final URI playerUri = BetrayalUtils.getRequestUriWithoutLastSegment(uriInfo);

		// Build response, filter to just send back ids and isTraitor status
		PlayerResponse response = mapperFacade.map(traitor, PlayerResponse.class);
		response.initFieldFiltering();
		response.setIncludeIsTraitor(true);

		return Response.ok(response).location(playerUri).build();
	}

	@GET
	@Path("/character/{character}/traitor")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTraitorStatusByCharacter(@PathParam("betrayalId") final String betrayalId, @PathParam("character") final String character) throws Exception {
		Character playerCharacter = Character.fromString(character);
		Player getPlayer = betrayalService.getPlayer(betrayalId, playerCharacter);

		// Filter response to just send back ids and isTraitor status
		PlayerResponse response = mapperFacade.map(getPlayer, PlayerResponse.class);
		response.initFieldFiltering();
		response.setIncludeIsTraitor(true);

		return Response.ok(response).build();
	}

	@POST
	@Path("/name/{name}/traitor")
	@Produces(MediaType.APPLICATION_JSON)
	public Response markAsTraitorByName(@PathParam("betrayalId") final String betrayalId, @PathParam("name") final String name) throws Exception {
		Player traitor = betrayalService.markAsTraitor(betrayalId, name);

		// Strip uri of the last segment and return location header pointing to player resource via name
		final URI playerUri = BetrayalUtils.getRequestUriWithoutLastSegment(uriInfo);

		// Filter response to just send back ids and isTraitor status
		PlayerResponse response = mapperFacade.map(traitor, PlayerResponse.class);
		response.initFieldFiltering();
		response.setIncludeIsTraitor(true);

		return Response.ok(response).location(playerUri).build();
	}

	@GET
	@Path("/name/{name}/traitor")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTraitorStatusByName(@PathParam("betrayalId") final String betrayalId, @PathParam("name") final String name) throws Exception {
		Player getPlayer = betrayalService.getPlayer(betrayalId, name);

		// Filter response to just send back ids and isTraitor status
		PlayerResponse response = mapperFacade.map(getPlayer, PlayerResponse.class);
		response.initFieldFiltering();
		response.setIncludeIsTraitor(true);

		return Response.ok(response).build();
	}

	@POST
	@Path("/character/{character}/hero")
	@Produces(MediaType.APPLICATION_JSON)
	public Response markAsHeroByCharacter(@PathParam("betrayalId") final String betrayalId, @PathParam("character") final String character) throws Exception {
		Character playerCharacter = Character.fromString(character);
		Player hero = betrayalService.markAsHero(betrayalId, playerCharacter);

		// Strip uri of the last segment and return location header pointing to player resource via character
		final URI playerUri = BetrayalUtils.getRequestUriWithoutLastSegment(uriInfo);

		// Build response, filter to just send back ids and isTraitor status
		PlayerResponse response = mapperFacade.map(hero, PlayerResponse.class);
		response.initFieldFiltering();
		response.setIncludeIsTraitor(true);

		return Response.ok(response).location(playerUri).build();
	}

	@GET
	@Path("/character/{character}/hero")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHeroStatusByCharacter(@PathParam("betrayalId") final String betrayalId, @PathParam("character") final String character) throws Exception {
		Character playerCharacter = Character.fromString(character);
		Player getPlayer = betrayalService.getPlayer(betrayalId, playerCharacter);

		// Filter response to just send back ids and isTraitor status
		PlayerResponse response = mapperFacade.map(getPlayer, PlayerResponse.class);
		response.initFieldFiltering();
		response.setIncludeIsTraitor(true);

		return Response.ok(response).build();
	}

	@POST
	@Path("/name/{name}/hero")
	@Produces(MediaType.APPLICATION_JSON)
	public Response markAsHeroByName(@PathParam("betrayalId") final String betrayalId, @PathParam("name") final String name) throws Exception {
		Player hero = betrayalService.markAsHero(betrayalId, name);

		// Strip uri of the last segment and return location header pointing to player resource via name
		final URI playerUri = BetrayalUtils.getRequestUriWithoutLastSegment(uriInfo);

		// Filter response to just send back ids and isTraitor status
		PlayerResponse response = mapperFacade.map(hero, PlayerResponse.class);
		response.initFieldFiltering();
		response.setIncludeIsTraitor(true);

		return Response.ok(response).location(playerUri).build();
	}

	@GET
	@Path("/name/{name}/hero")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHeroStatusByName(@PathParam("betrayalId") final String betrayalId, @PathParam("name") final String name) throws Exception {
		Player getPlayer = betrayalService.getPlayer(betrayalId, name);

		// Filter response to just send back ids and isTraitor status
		PlayerResponse response = mapperFacade.map(getPlayer, PlayerResponse.class);
		response.initFieldFiltering();
		response.setIncludeIsTraitor(true);

		return Response.ok(response).build();
	}

	@POST
	@Path("/character/{character}/speed")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response speedStatRequestByCharacter(@PathParam("betrayalId") final String betrayalId, @PathParam("character") final String character, final StatRequest statRequest) throws Exception {
		Character playerCharacter = Character.fromString(character);
		Player player = statRequestByCharacter(statRequest, Stat.SPEED, betrayalId, playerCharacter);

		// Strip uri of the last segment and return location header pointing to player resource via name
		final URI playerUri = BetrayalUtils.getRequestUriWithoutLastSegment(uriInfo);

		// Filter response to just send back ids and speed value
		PlayerResponse response = mapperFacade.map(player, PlayerResponse.class);
		response.initFieldFiltering();
		response.setIncludeSpeed(true);
		if (response.getSpeed() == 0) {
			response.setIncludeIsAlive(true);
		}

		return Response.ok(response).location(playerUri).build();
	}

	@GET
	@Path("/character/{character}/speed")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSpeedByCharacter(@PathParam("betrayalId") final String betrayalId, @PathParam("character") final String character) throws Exception {
		Character playerCharacter = Character.fromString(character);
		Player getPlayer = betrayalService.getPlayer(betrayalId, playerCharacter);

		// Filter response to just send back ids and speed value
		PlayerResponse response = mapperFacade.map(getPlayer, PlayerResponse.class);
		response.initFieldFiltering();
		response.setIncludeSpeed(true);

		return Response.ok(response).build();
	}

	@POST
	@Path("/name/{name}/speed")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response speedStatRequestByName(@PathParam("betrayalId") final String betrayalId, @PathParam("name") final String name, final StatRequest statRequest) throws Exception {
		Player player = statRequestByName(statRequest, Stat.SPEED, betrayalId, name);

		// Strip uri of the last segment and return location header pointing to player resource via name
		final URI playerUri = BetrayalUtils.getRequestUriWithoutLastSegment(uriInfo);

		// Filter response to just send back ids and speed value
		PlayerResponse response = mapperFacade.map(player, PlayerResponse.class);
		response.initFieldFiltering();
		response.setIncludeSpeed(true);
		if (response.getSpeed() == 0) {
			response.setIncludeIsAlive(true);
		}

		return Response.ok(response).location(playerUri).build();
	}

	@GET
	@Path("/name/{name}/speed")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSpeedByName(@PathParam("betrayalId") final String betrayalId, @PathParam("name") final String name) throws Exception {
		Player getPlayer = betrayalService.getPlayer(betrayalId, name);

		// Filter response to just send back ids and speed value
		PlayerResponse response = mapperFacade.map(getPlayer, PlayerResponse.class);
		response.initFieldFiltering();
		response.setIncludeSpeed(true);

		return Response.ok(response).build();
	}

	@POST
	@Path("/character/{character}/might")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response mightStatRequestByCharacter(@PathParam("betrayalId") final String betrayalId, @PathParam("character") final String character, final StatRequest statRequest) throws Exception {
		Character playerCharacter = Character.fromString(character);
		Player player = statRequestByCharacter(statRequest, Stat.MIGHT, betrayalId, playerCharacter);

		// Strip uri of the last segment and return location header pointing to player resource via name
		final URI playerUri = BetrayalUtils.getRequestUriWithoutLastSegment(uriInfo);

		// Filter response to just send back ids and might value
		PlayerResponse response = mapperFacade.map(player, PlayerResponse.class);
		response.initFieldFiltering();
		response.setIncludeMight(true);
		if (response.getMight() == 0) {
			response.setIncludeIsAlive(true);
		}

		return Response.ok(response).location(playerUri).build();
	}

	@GET
	@Path("/character/{character}/might")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMightByCharacter(@PathParam("betrayalId") final String betrayalId, @PathParam("character") final String character) throws Exception {
		Character playerCharacter = Character.fromString(character);
		Player getPlayer = betrayalService.getPlayer(betrayalId, playerCharacter);

		// Filter response to just send back ids and might value
		PlayerResponse response = mapperFacade.map(getPlayer, PlayerResponse.class);
		response.initFieldFiltering();
		response.setIncludeMight(true);

		return Response.ok(response).build();
	}

	@POST
	@Path("/name/{name}/might")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response mightStatRequestByName(@PathParam("betrayalId") final String betrayalId, @PathParam("name") final String name, final StatRequest statRequest) throws Exception {
		Player player = statRequestByName(statRequest, Stat.MIGHT, betrayalId, name);

		// Strip uri of the last segment and return location header pointing to player resource via name
		final URI playerUri = BetrayalUtils.getRequestUriWithoutLastSegment(uriInfo);

		// Filter response to just send back ids and might value
		PlayerResponse response = mapperFacade.map(player, PlayerResponse.class);
		response.initFieldFiltering();
		response.setIncludeMight(true);
		if (response.getMight() == 0) {
			response.setIncludeIsAlive(true);
		}

		return Response.ok(response).location(playerUri).build();
	}

	@GET
	@Path("/name/{name}/might")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMightByName(@PathParam("betrayalId") final String betrayalId, @PathParam("name") final String name) throws Exception {
		Player getPlayer = betrayalService.getPlayer(betrayalId, name);

		// Filter response to just send back ids and might value
		PlayerResponse response = mapperFacade.map(getPlayer, PlayerResponse.class);
		response.initFieldFiltering();
		response.setIncludeMight(true);

		return Response.ok(response).build();
	}

	@POST
	@Path("/character/{character}/sanity")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response sanityStatRequestByCharacter(@PathParam("betrayalId") final String betrayalId, @PathParam("character") final String character, final StatRequest statRequest) throws Exception {
		Character playerCharacter = Character.fromString(character);
		Player player = statRequestByCharacter(statRequest, Stat.SANITY, betrayalId, playerCharacter);

		// Strip uri of the last segment and return location header pointing to player resource via name
		final URI playerUri = BetrayalUtils.getRequestUriWithoutLastSegment(uriInfo);

		// Filter response to just send back ids and sanity value
		PlayerResponse response = mapperFacade.map(player, PlayerResponse.class);
		response.initFieldFiltering();
		response.setIncludeSanity(true);
		if (response.getSanity() == 0) {
			response.setIncludeIsAlive(true);
		}

		return Response.ok(response).location(playerUri).build();
	}

	@GET
	@Path("/character/{character}/sanity")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSanityByCharacter(@PathParam("betrayalId") final String betrayalId, @PathParam("character") final String character) throws Exception {
		Character playerCharacter = Character.fromString(character);
		Player getPlayer = betrayalService.getPlayer(betrayalId, playerCharacter);

		// Filter response to just send back ids and sanity value
		PlayerResponse response = mapperFacade.map(getPlayer, PlayerResponse.class);
		response.initFieldFiltering();
		response.setIncludeSanity(true);

		return Response.ok(response).build();
	}

	@POST
	@Path("/name/{name}/sanity")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response sanityStatRequestByName(@PathParam("betrayalId") final String betrayalId, @PathParam("name") final String name, final StatRequest statRequest) throws Exception {
		Player player = statRequestByName(statRequest, Stat.SANITY, betrayalId, name);

		// Strip uri of the last segment and return location header pointing to player resource via name
		final URI playerUri = BetrayalUtils.getRequestUriWithoutLastSegment(uriInfo);

		// Filter response to just send back ids and sanity value
		PlayerResponse response = mapperFacade.map(player, PlayerResponse.class);
		response.initFieldFiltering();
		response.setIncludeSanity(true);
		if (response.getSanity() == 0) {
			response.setIncludeIsAlive(true);
		}

		return Response.ok(response).location(playerUri).build();
	}

	@GET
	@Path("/name/{name}/sanity")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSanityByName(@PathParam("betrayalId") final String betrayalId, @PathParam("name") final String name) throws Exception {
		Player getPlayer = betrayalService.getPlayer(betrayalId, name);

		// Filter response to just send back ids and sanity value
		PlayerResponse response = mapperFacade.map(getPlayer, PlayerResponse.class);
		response.initFieldFiltering();
		response.setIncludeSanity(true);

		return Response.ok(response).build();
	}

	@POST
	@Path("/character/{character}/knowledge")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response knowledgeStatRequestByCharacter(@PathParam("betrayalId") final String betrayalId, @PathParam("character") final String character, final StatRequest statRequest) throws Exception {
		Character playerCharacter = Character.fromString(character);
		Player player = statRequestByCharacter(statRequest, Stat.KNOWLEDGE, betrayalId, playerCharacter);

		// Strip uri of the last segment and return location header pointing to player resource via name
		final URI playerUri = BetrayalUtils.getRequestUriWithoutLastSegment(uriInfo);

		// Filter response to just send back ids and knowledge value
		PlayerResponse response = mapperFacade.map(player, PlayerResponse.class);
		response.initFieldFiltering();
		response.setIncludeKnowledge(true);
		if (response.getKnowledge() == 0) {
			response.setIncludeIsAlive(true);
		}

		return Response.ok(response).location(playerUri).build();
	}

	@GET
	@Path("/character/{character}/knowledge")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getKnowledgeByCharacter(@PathParam("betrayalId") final String betrayalId, @PathParam("character") final String character) throws Exception {
		Character playerCharacter = Character.fromString(character);
		Player getPlayer = betrayalService.getPlayer(betrayalId, playerCharacter);

		// Filter response to just send back ids and knowledge value
		PlayerResponse response = mapperFacade.map(getPlayer, PlayerResponse.class);
		response.initFieldFiltering();
		response.setIncludeKnowledge(true);

		return Response.ok(response).build();
	}

	@POST
	@Path("/name/{name}/knowledge")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response knowledgeStatRequestByName(@PathParam("betrayalId") final String betrayalId, @PathParam("name") final String name, final StatRequest statRequest) throws Exception {
		Player player = statRequestByName(statRequest, Stat.KNOWLEDGE, betrayalId, name);

		// Strip uri of the last segment and return location header pointing to player resource via name
		final URI playerUri = BetrayalUtils.getRequestUriWithoutLastSegment(uriInfo);

		// Filter response to just send back ids and knowledge value
		PlayerResponse response = mapperFacade.map(player, PlayerResponse.class);
		response.initFieldFiltering();
		response.setIncludeKnowledge(true);
		if (response.getKnowledge() == 0) {
			response.setIncludeIsAlive(true);
		}

		return Response.ok(response).location(playerUri).build();
	}

	@GET
	@Path("/name/{name}/knowledge")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getKnowledgeByName(@PathParam("betrayalId") final String betrayalId, @PathParam("name") final String name) throws Exception {
		Player getPlayer = betrayalService.getPlayer(betrayalId, name);

		// Filter response to just send back ids, knowledge value
		PlayerResponse response = mapperFacade.map(getPlayer, PlayerResponse.class);
		response.initFieldFiltering();
		response.setIncludeKnowledge(true);

		return Response.ok(response).build();
	}

	@DELETE
	@Path("/character/{character}")
	public Response deletePlayerByCharacter(@PathParam("betrayalId") final String betrayalId, @PathParam("character") final String character) throws Exception {
		Character playerCharacter = Character.fromString(character);
		betrayalService.deletePlayer(betrayalId, playerCharacter);
		return Response.noContent().build();
	}

	@DELETE
	@Path("/name/{name}")
	public Response deletePlayerByName(@PathParam("betrayalId") final String betrayalId, @PathParam("name") final String name) throws Exception {
		betrayalService.deletePlayer(betrayalId, name);
		return Response.noContent().build();
	}

	private Player statRequestByCharacter(StatRequest statRequest, Stat stat, String betrayalId, Character playerCharacter) throws BetrayalException {
		switch (statRequest.getStatOperation()) {
			case RESET:
				return betrayalService.resetStatIndex(betrayalId, playerCharacter, stat);
			case INCREMENT:
				return betrayalService.incrementStatIndex(betrayalId, playerCharacter, stat);
			case DECREMENT:
				return betrayalService.decrementStatIndex(betrayalId, playerCharacter, stat);

		}
		return null;
	}

	private Player statRequestByName(StatRequest statRequest, Stat stat, String betrayalId, String name) throws BetrayalException {
		switch (statRequest.getStatOperation()) {
			case RESET:
				return betrayalService.resetStatIndex(betrayalId, name, stat);
			case INCREMENT:
				return betrayalService.incrementStatIndex(betrayalId, name, stat);
			case DECREMENT:
				return betrayalService.decrementStatIndex(betrayalId, name, stat);

		}
		return null;
	}
}