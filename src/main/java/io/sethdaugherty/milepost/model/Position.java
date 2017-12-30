package io.sethdaugherty.milepost.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;

/**
 * PERSISTANCE model to represent a tuple of <timestamp, lat, long>
 */
@Entity
public class Position {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	@Getter private long timestamp;
	@Getter private double latitude;
	@Getter private double longitude;
	
	public Position() {}
	
	public Position(long timestamp, double latitude, double longitude) {
		this.timestamp = timestamp;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public static Position fromExternal(io.sethdaugherty.milepost.model.external.Position positionExternal) {
		return new Position(positionExternal.getTimestamp(), positionExternal.getLatitude(), positionExternal.getLongitude());
	}
}
