package com.github.aawkall.betrayalapi.resource;

import java.net.URI;

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

import com.github.aawkall.betrayalapi.entity.db.Betrayal;
import com.github.aawkall.betrayalapi.entity.request.CreateBetrayal;
import com.github.aawkall.betrayalapi.entity.request.SetHauntNumber;
import com.github.aawkall.betrayalapi.entity.response.BetrayalResponse;
import com.github.aawkall.betrayalapi.service.BetrayalService;
import com.github.aawkall.betrayalapi.util.BetrayalUtils;

import ma.glasnost.orika.MapperFacade;

@Path("/betrayalapi/betrayal")
@Produces(MediaType.APPLICATION_JSON)
public class BetrayalResource {

	private final BetrayalService betrayalService;
	private final MapperFacade mapperFacade;

	@Context
	private UriInfo uriInfo;

	@Inject
	public BetrayalResource(final BetrayalService betrayalService, final MapperFacade mapperFacade) {
		this.betrayalService = betrayalService;
		this.mapperFacade = mapperFacade;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createBetrayalGame(final CreateBetrayal createBetrayal) throws Exception {
		Betrayal betrayal = betrayalService.createBetrayalGame(createBetrayal.getBetrayalId());

		final URI betrayalUri = UriBuilder.fromUri(uriInfo.getRequestUri())
				.path("{betrayalId}")
				.resolveTemplate("betrayalId", betrayal.getBetrayalId())
				.build();
		BetrayalResponse response = mapperFacade.map(betrayal, BetrayalResponse.class);
		return Response.created(betrayalUri).entity(response).build();
	}

	@GET
	@Path("/{betrayalId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBetrayalGame(@PathParam("betrayalId") final String betrayalId) throws Exception {
		Betrayal getBetrayal = betrayalService.getBetrayalGame(betrayalId);
		BetrayalResponse response = mapperFacade.map(getBetrayal, BetrayalResponse.class);
		return Response.ok(response).build();
	}

	@GET
	@Path("/{betrayalId}/hauntnumber")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHauntNumber(@PathParam("betrayalId") final String betrayalId) throws Exception {
		Betrayal getBetrayal = betrayalService.getBetrayalGame(betrayalId);

		// Filter response to just return haunt number and betrayalId
		BetrayalResponse response = mapperFacade.map(getBetrayal, BetrayalResponse.class);
		response.initFieldFiltering();
		response.setIncludeHauntNumber(true);

		return Response.ok(response).build();
	}

	@POST
	@Path("/{betrayalId}/hauntnumber")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response setHauntNumber(@PathParam("betrayalId") final String betrayalId, final SetHauntNumber setHauntNumber) throws Exception {
		Betrayal betrayal = betrayalService.setHauntNumber(betrayalId, setHauntNumber.getHauntNumber());

		// Strip uri of the last segment and return location header pointing to betrayal resource
		final URI betrayalUri = BetrayalUtils.getRequestUriWithoutLastSegment(uriInfo);

		// Filter response to just return haunt number and betrayalId
		BetrayalResponse response = mapperFacade.map(betrayal, BetrayalResponse.class);
		response.initFieldFiltering();
		response.setIncludeHauntNumber(true);

		return Response.ok(response).location(betrayalUri).build();
	}

	@DELETE
	@Path("/{betrayalId}")
	public Response deleteBetrayalGame(@PathParam("betrayalId") final String betrayalId) throws Exception {
		betrayalService.deleteBetrayalGame(betrayalId);
		return Response.noContent().build();
	}
}