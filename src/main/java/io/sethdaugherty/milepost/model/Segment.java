package io.sethdaugherty.milepost.model;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import lombok.Getter;

public class Segment {

    @Getter private final List<Position> positionList;
    
    public Segment(List<Position> positionList) {
        this.positionList = positionList;
    }

    public static io.sethdaugherty.milepost.model.external.Segment toExternal(Segment segment) {
        List<io.sethdaugherty.milepost.model.external.Position> externalPositionList = segment.getPositionList().stream()
                .map(Position::toExternal).collect(Collectors.toList());
        io.sethdaugherty.milepost.model.external.Segment externalSegment = new io.sethdaugherty.milepost.model.external.Segment();
        externalSegment.setPositions(externalPositionList);
        return externalSegment;
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
