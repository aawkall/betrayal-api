package com.github.aawkall.betrayalapi.resource;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.aawkall.betrayalapi.exception.BetrayalException;
import com.github.aawkall.betrayalapi.service.BetrayalService;

@Path("/betrayalapi")
public class BetrayalResource {
	private static final Logger log = LogManager.getLogger(BetrayalResource.class);

	private final BetrayalService betrayalService;

	@Context
	private UriInfo uriInfo;

	@Inject
	public BetrayalResource(final BetrayalService betrayalService) {
		this.betrayalService = betrayalService;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response functionRunner() throws BetrayalException {
		return Response.ok().build();
	}
}