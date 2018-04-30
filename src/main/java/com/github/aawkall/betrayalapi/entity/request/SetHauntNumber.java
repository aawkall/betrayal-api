package com.github.aawkall.betrayalapi.entity.request;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class SetHauntNumber {

	private int hauntNumber;

	public int getHauntNumber() {
		return hauntNumber;
	}

	public void setHauntNumber(int hauntNumber) {
		this.hauntNumber = hauntNumber;
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
		return String.format("SetHauntNumber[hauntNumber=%s]", hauntNumber);
	}
}