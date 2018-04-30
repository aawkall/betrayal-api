package com.github.aawkall.betrayalapi.entity.request;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class StatRequest {

	public enum StatOperation {
		RESET,
		INCREMENT,
		DECREMENT
	}

	private StatOperation statOperation;

	public StatOperation getStatOperation() {
		return statOperation;
	}

	public void setStatOperation(StatOperation statOperation) {
		this.statOperation = statOperation;
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
		return String.format("StatRequest[statOperation=%s]", statOperation);
	}
}