package io.sethdaugherty.milepost.api.data.analysis;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import io.sethdaugherty.milepost.model.Position;
import io.sethdaugherty.milepost.model.Segment;

public class SpeedBasedSegmentOutlierDetectorTest {

    @Test
    public void testRemoveOutliers_emptySegment() {
        SpeedBasedSegmentOutlierDetector detector = new SpeedBasedSegmentOutlierDetector();
        
        Segment inputSegment = new Segment(Collections.EMPTY_LIST);
        Segment actualSegment = detector.removeOutliers(inputSegment);
        Segment expectedSegment = new Segment(Collections.EMPTY_LIST);
        
        Assert.assertEquals(expectedSegment, actualSegment);
    }

    @Test
    public void testRemoveOutliers_noOutliers() {
        SpeedBasedSegmentOutlierDetector detector = new SpeedBasedSegmentOutlierDetector();
        
        List<Position> positions = new ArrayList<>();
        positions.add(new Position(1, 0, 0));
        positions.add(new Position(2, 1, 0));
        positions.add(new Position(3, 0, 1));
        positions.add(new Position(4, 0, 0));
        positions.add(new Position(5, 1, 1));
        positions.add(new Position(6, 0.1, 1.1));
        positions.add(new Position(7, 1.1, 0));
        positions.add(new Position(8, .2, .2));
        
        Segment inputSegment = new Segment(positions);
        Segment actualSegment = detector.removeOutliers(inputSegment);
        Segment expectedSegment = new Segment(positions);
        
        Assert.assertEquals(expectedSegment, actualSegment);
    }

    @Test
    public void testRemoveOutliers_twoOutliers() {
        SpeedBasedSegmentOutlierDetector detector = new SpeedBasedSegmentOutlierDetector();
        
        List<Position> positions = new ArrayList<>();
        positions.add(new Position(1, 0, 0));
        positions.add(new Position(2, 1, 0));
        positions.add(new Position(3, 0, 1));
        positions.add(new Position(4, 0, 0));
        positions.add(new Position(5, 1, 1));
        positions.add(new Position(6, 1000, 1.1));
        positions.add(new Position(7, 1.1, 1.1));
        positions.add(new Position(8, .2, .2));
        positions.add(new Position(9, 0, 0.1));
        positions.add(new Position(10, 1.1, 0.2));
        positions.add(new Position(11, 0, 1.4));
        positions.add(new Position(12, 0, 1000));
        positions.add(new Position(13, 1, 1.2));
        positions.add(new Position(14, 0, 0));
        positions.add(new Position(15, 1, 0));
        positions.add(new Position(16, 0, 1));
        positions.add(new Position(17, 0, 0));
        positions.add(new Position(18, 1, 1));
        positions.add(new Position(19, 1, 1.1));
        positions.add(new Position(20, 1.1, 5));
        positions.add(new Position(21, .2, .2));
        positions.add(new Position(22, 0, 0.1));
        positions.add(new Position(23, 1.1, 0.2));
        positions.add(new Position(24, 0, 1.4));
        positions.add(new Position(25, 0, 0.3));
        positions.add(new Position(26, 1, 1.2));
        
        Segment inputSegment = new Segment(positions);
        Segment actualSegment = detector.removeOutliers(inputSegment);
        List<Position> expectedPositions = new ArrayList<>();
        expectedPositions.add(positions.get(0));
        expectedPositions.add(positions.get(1));
        expectedPositions.add(positions.get(2));
        expectedPositions.add(positions.get(3));
        expectedPositions.add(positions.get(4));
        // outlier
        //expectedPositions.add(positions.get(5));
        expectedPositions.add(positions.get(6));
        expectedPositions.add(positions.get(7));
        expectedPositions.add(positions.get(8));
        expectedPositions.add(positions.get(9));
        expectedPositions.add(positions.get(10));
        // outlier
        //expectedPositions.add(positions.get(11));
        expectedPositions.add(positions.get(12));
        expectedPositions.add(positions.get(13));
        expectedPositions.add(positions.get(14));
        expectedPositions.add(positions.get(15));
        expectedPositions.add(positions.get(16));
        expectedPositions.add(positions.get(17));
        expectedPositions.add(positions.get(18));
        expectedPositions.add(positions.get(19));
        expectedPositions.add(positions.get(20));
        expectedPositions.add(positions.get(21));
        expectedPositions.add(positions.get(22));
        expectedPositions.add(positions.get(23));
        expectedPositions.add(positions.get(24));
        expectedPositions.add(positions.get(25));

        
        Segment expectedSegment = new Segment(expectedPositions);
        
        Assert.assertEquals(expectedSegment.getPositionList().size(), actualSegment.getPositionList().size());
        Assert.assertEquals(expectedSegment, actualSegment);
    }

}
