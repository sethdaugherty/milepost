package io.sethdaugherty.milepost.playtime.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;

@Entity
public class Playtime {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Getter private long timestampStarted;
	@Getter private long millisSpent;
	@Getter private String date;
	@Getter private String url;
	
	public Playtime() {}

	public Playtime(long timestampStarted, long millisSpent, String date,  String url) {
		this.timestampStarted = timestampStarted;
		this.millisSpent = millisSpent;
		this.date = date;
		this.url = url;
	}
}
