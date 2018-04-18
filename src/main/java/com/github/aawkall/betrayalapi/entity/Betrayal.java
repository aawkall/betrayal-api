package com.github.aawkall.betrayalapi.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Document(collection = "games")
public final class Betrayal {

	@Id
	private String _id;

	@Indexed
	private String channelId;

	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date timeCreated;

	private int hauntNumber;

	public Betrayal(String channelId, Date timeCreated) {
		this.channelId = channelId;
		this.timeCreated = timeCreated;
		this.hauntNumber = 0;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
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
	public String toString() {
		return String.format("Betrayal[_id=%s, channelId=%s, timeCreated=%s, hauntNumber=%s]", _id, channelId, timeCreated, hauntNumber);
	}
}