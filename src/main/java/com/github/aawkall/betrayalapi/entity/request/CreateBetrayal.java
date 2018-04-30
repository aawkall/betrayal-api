package com.github.aawkall.betrayalapi.entity.request;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class CreateBetrayal {

	private String betrayalId;

	public String getBetrayalId() {
		return betrayalId;
	}

	public void setBetrayalId(String betrayalId) {
		this.betrayalId = betrayalId;
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
		return String.format("CreateBetrayal[betrayalId=%s]", betrayalId);
	}
}