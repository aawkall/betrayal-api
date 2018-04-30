package com.github.aawkall.betrayalapi.util;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriInfo;

import com.github.aawkall.betrayalapi.entity.db.Player;
import com.github.aawkall.betrayalapi.entity.response.PlayerResponse;

import ma.glasnost.orika.MapperFacade;

public class BetrayalUtils {

	public static List<PlayerResponse> mapPlayerList(List<Player> players, MapperFacade mapperFacade) {
		List<PlayerResponse> playerResponses = new ArrayList<>();
		for (Player player : players) {
			playerResponses.add(mapperFacade.map(player, PlayerResponse.class));
		}
		return playerResponses;
	}

	public static URI getRequestUriWithoutLastSegment(UriInfo uriInfo) throws Exception {
		URL requestUrl = uriInfo.getRequestUri().toURL();
		URL entityUrl = new URL(requestUrl.toString().substring(0, requestUrl.toString().lastIndexOf('/')));
		return entityUrl.toURI();
	}

	// DELETE:
	//     - returns a 204, no content
	//     - no Location header
	// GET the object itself (path ends in betrayalId / character / name):
	//     - returns a 200, entity includes the full ObjectResponse object (mapped from the DB entity, with all serialized values included)
	//     - no Location header
	// GET one of the sub-attributes of the object, like the speed stat, or the hauntnumber:
	//     - returns a 200, entity includes the ObjectResponse object (mapped from the DB entity), but with the flags set on the object to only include the id and the requested attribute
	//     - no Location header
	// CREATE:
	//     - returns a 201, entity includes the full ObjectResponse object (mapped from the DB entity, with all serialized values included)
	//     - Location header points to the full object URI
	// POST to one of the sub-attributes of the object, like the speed stat, or the hauntnumber:
	//     - returns a 200, entity includes the ObjectResponse object (mapped from the DB entity), but with the flags set on the object to only include the id and the requested attribute
	//     - Location header points to the full object URI, without the sub-attribute in the URI
}