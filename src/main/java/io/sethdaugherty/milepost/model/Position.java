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
    @Getter
    private Long id;
    @Getter
    private long timestamp;
    @Getter
    private double latitude;
    @Getter
    private double longitude;
    @Getter
    private Double accuracy;

    public Position() {
    }

    public Position(long timestamp, double latitude, double longitude, Double accuracy) {
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;
        this.id = ThreadLocalRandom.current().nextLong();
    }

    public Position(long id, long timestamp, double latitude, double longitude, Double accuracy) {
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;
        this.id = id;
    }

    public static Position fromExternal(io.sethdaugherty.milepost.model.external.Position positionExternal) {
        return new Position(positionExternal.getTimestamp(), positionExternal.getLatitude(),
                positionExternal.getLongitude(), positionExternal.getAccuracy());
    }

    public static io.sethdaugherty.milepost.model.external.Position toExternal(Position positionInternal) {
        io.sethdaugherty.milepost.model.external.Position positionExternal = new io.sethdaugherty.milepost.model.external.Position();
        positionExternal.setTimestamp(positionInternal.getTimestamp());
        positionExternal.setLatitude(positionInternal.getLatitude());
        positionExternal.setLongitude(positionInternal.getLongitude());
        positionExternal.setAccuracy(positionInternal.getAccuracy());

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
                && Objects.equals(this.longitude, otherPosition.longitude)
                && Objects.equals(this.accuracy, otherPosition.accuracy);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, timestamp, latitude, longitude, accuracy);
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
