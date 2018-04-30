package com.github.aawkall.betrayalapi.entity.response;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

@JsonSerialize(using = BetrayalResponse.BetrayalSerializer.class)
public final class BetrayalResponse {

	private String betrayalId;
	private Date timeCreated;
	private int hauntNumber;

	private boolean includeBetrayalId;
	private boolean includeTimeCreated;
	private boolean includeHauntNumber;

	public BetrayalResponse() {
		// All fields are included by default
		includeBetrayalId = true;
		includeTimeCreated = true;
		includeHauntNumber = true;
	}

	public String getBetrayalId() {
		return betrayalId;
	}

	public void setBetrayalId(String betrayalId) {
		this.betrayalId = betrayalId;
	}

	public Date getTimeCreated() {
		return timeCreated;
	}

	public void setTimeCreated(Date timeCreated) {
		this.timeCreated = timeCreated;
	}

	public int getHauntNumber() {
		return hauntNumber;
	}

	public void setHauntNumber(int hauntNumber) {
		this.hauntNumber = hauntNumber;
	}

	public void initFieldFiltering() {
		/* When filtering, betrayalId is included by default, and other fields are not included
		   Fields can be included by calling the individual "include" methods. The include variables
		   are used when the object is serialized */
		includeBetrayalId = true;
		includeTimeCreated = false;
		includeHauntNumber = false;
	}

	public void setIncludeBetrayalId(boolean includeBetrayalId) {
		this.includeBetrayalId = includeBetrayalId;
	}

	public boolean isBetrayalIdIncluded() {
		return includeBetrayalId;
	}

	public void setIncludeTimeCreated(boolean includeTimeCreated) {
		this.includeTimeCreated = includeTimeCreated;
	}

	public boolean isTimeCreatedIncluded() {
		return includeTimeCreated;
	}

	public void setIncludeHauntNumber(boolean includeHauntNumber) {
		this.includeHauntNumber = includeHauntNumber;
	}

	public boolean isHauntNumberIncluded() {
		return includeHauntNumber;
	}

	@Override
	public boolean equals(Object object) {
		return EqualsBuilder.reflectionEquals(object, this);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public String toString() {
		return String.format("BetrayalResponse[betrayalId=%s, timeCreated=%s, hauntNumber=%s]", betrayalId, timeCreated, hauntNumber);
	}

	public static class BetrayalSerializer extends StdSerializer<BetrayalResponse> {
		private static final long serialVersionUID = 1L;

		@SuppressWarnings("unused")
		public BetrayalSerializer() {
			this(null);
		}

		public BetrayalSerializer(Class<BetrayalResponse> t) {
			super(t);
		}

		@Override
		public void serialize(BetrayalResponse betrayalResponse, JsonGenerator jgen, SerializerProvider sp)
				throws IOException, JsonProcessingException {
			jgen.writeStartObject();

			if (betrayalResponse.isBetrayalIdIncluded()) {
				jgen.writeStringField("betrayalId", betrayalResponse.getBetrayalId());
			}
			if (betrayalResponse.isTimeCreatedIncluded()) {
				jgen.writeStringField("timeCreated", betrayalResponse.getTimeCreated().toString());
			}
			if (betrayalResponse.isHauntNumberIncluded()) {
				jgen.writeNumberField("hauntNumber", betrayalResponse.getHauntNumber());
			}

			jgen.writeEndObject();
		}
	}
}