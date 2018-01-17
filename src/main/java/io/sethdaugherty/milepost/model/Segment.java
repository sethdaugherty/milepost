package io.sethdaugherty.milepost.model;

import java.util.List;
import java.util.Objects;

import lombok.Getter;

public class Segment {

    @Getter private final List<Position> positionList;
    
    public Segment(List<Position> positionList) {
        this.positionList = positionList;
    }
    
    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (getClass() != other.getClass()) {
            return false;
        }
        final Segment otherSegment = (Segment) other;
        return Objects.equals(this.positionList, otherSegment.positionList);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(positionList);
    }
}
