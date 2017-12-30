package io.sethdaugherty.milepost.model.external;

import lombok.Getter;
import lombok.Setter;

/**
 * EXTERNAL model to represent a tuple of <timestamp, lat, long>
 */
public class Position {
	
	@Getter @Setter private long timestamp;
	@Getter @Setter private double latitude;
	@Getter @Setter private double longitude;
}
