package io.sethdaugherty.milepost.model.external;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * EXTERNAL model to represent a tuple of <timestamp, lat, long>
 */
public class Segment {
    
    @Getter @Setter private List<Position> positions;
}
