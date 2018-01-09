package io.sethdaugherty.milepost.playtime.model;

import lombok.Getter;

public class AddPlaytimeRequest {
	@Getter private long timestampStarted;
	@Getter private long millisSpent;
	@Getter private String url;
	
	public AddPlaytimeRequest() {}

	public AddPlaytimeRequest(long timestampStarted, long millisSpent, String url) {
		this.timestampStarted = timestampStarted;
		this.millisSpent = millisSpent;
		this.url = url;
	}
}
