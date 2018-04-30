package com.github.aawkall.betrayalapi.entity.db;

import java.util.Date;
import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.github.aawkall.betrayalapi.util.BetrayalConst;

@Document(collection = BetrayalConst.BETRAYAL_COLLECTION)
public final class Betrayal {

	@Id
	private String _id;

	@Indexed
	private String betrayalId;

	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date timeCreated;

	private int hauntNumber;

	public Betrayal(String betrayalId, Date timeCreated) {
		this.betrayalId = betrayalId;
		this.timeCreated = timeCreated;
		this.hauntNumber = 0;
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

	@Override
	public boolean equals(Object object) {
		if (object == this) {
			return true;
		}
		if (!(object instanceof Betrayal)) {
			return false;
		}

		Betrayal toCompare = (Betrayal) object;
		return Objects.equals(betrayalId, toCompare.getBetrayalId())
				&& Objects.equals(timeCreated, toCompare.getTimeCreated())
				&& hauntNumber == toCompare.hauntNumber;
	}

	@Override
	public String toString() {
		return String.format("Betrayal[_id=%s, betrayalId=%s, timeCreated=%s, hauntNumber=%s]", _id, betrayalId, timeCreated, hauntNumber);
	}
}