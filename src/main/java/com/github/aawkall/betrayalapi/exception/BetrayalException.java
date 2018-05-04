package com.github.aawkall.betrayalapi.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Provider
public class BetrayalException extends Exception implements ExceptionMapper<BetrayalException> {
	private static final long serialVersionUID = -5661328967803963711L;

	private static final Logger log = LogManager.getLogger(BetrayalException.class);

	private Response.Status httpCode;

	public BetrayalException() {
		super();
	}

	public BetrayalException(String msg, Response.Status httpCode) {
		super(msg);
		this.httpCode = httpCode;
	}

	public BetrayalException(Throwable cause, Response.Status httpCode) {
		super(cause);
		this.httpCode = httpCode;
	}

	public BetrayalException(String msg, Throwable cause, Response.Status httpCode) {
		super(msg, cause);
		this.httpCode = httpCode;
	}

	public Response.Status getHttpCode() {
		return httpCode;
	}

	@Override
	public Response toResponse(BetrayalException exception) {
		int statusCode = exception.getHttpCode().getStatusCode();
		log.error("BetrayalException: {}, sending http code: {}", exception.getMessage(), statusCode);
		return Response.status(statusCode).entity("{ \"error\": \"" + exception.getMessage() + "\" }").type("application/json").build();
	}
}