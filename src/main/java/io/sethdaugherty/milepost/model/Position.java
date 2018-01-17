package io.sethdaugherty.milepost.model;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;

/**
 * PERSISTANCE model to represent a tuple of <timestamp, lat, long>
 */
@Entity
public class Position implements Comparable<Position>{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Getter
    private long timestamp;
    @Getter
    private double latitude;
    @Getter
    private double longitude;

    public Position() {
    }

    public Position(long timestamp, double latitude, double longitude) {
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude = longitude;
        this.id = ThreadLocalRandom.current().nextLong();
    }

    public static Position fromExternal(io.sethdaugherty.milepost.model.external.Position positionExternal) {
        return new Position(positionExternal.getTimestamp(), positionExternal.getLatitude(),
                positionExternal.getLongitude());
    }

    public static io.sethdaugherty.milepost.model.external.Position toExternal(Position positionInternal) {
        io.sethdaugherty.milepost.model.external.Position positionExternal = new io.sethdaugherty.milepost.model.external.Position();
        positionExternal.setTimestamp(positionInternal.getTimestamp());
        positionExternal.setLatitude(positionInternal.getLatitude());
        positionExternal.setLongitude(positionInternal.getLongitude());

        return positionExternal;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (getClass() != other.getClass()) {
            return false;
        }
        final Position otherPosition = (Position) other;
        return Objects.equals(this.id, otherPosition.id)
                && Objects.equals(this.timestamp, otherPosition.timestamp)
                && Objects.equals(this.latitude, otherPosition.latitude)
                && Objects.equals(this.longitude, otherPosition.longitude);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, timestamp, latitude, longitude);
    }

    @Override
    public int compareTo(Position other) {
        if (this.timestamp == other.timestamp) {
            return 0;
        }
        
        if (this.timestamp < other.timestamp) {
            return -1;
        }
        
        return 1;
    }
}
