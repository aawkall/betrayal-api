package com.github.aawkall.betrayalapi.config;

import javax.servlet.ServletContext;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Context;

import org.glassfish.jersey.server.ResourceConfig;

import com.github.aawkall.betrayalapi.exception.BetrayalException;
import com.github.aawkall.betrayalapi.resource.BetrayalResource;

@ApplicationPath("/")
public class JerseyConfiguration extends ResourceConfig {

	public JerseyConfiguration(@Context ServletContext servletContext) {
		register(BetrayalResource.class);
		register(BetrayalException.class);
	}
}