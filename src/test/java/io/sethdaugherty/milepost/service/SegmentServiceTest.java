package io.sethdaugherty.milepost.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.Test;
import org.mockito.Mockito;

import io.sethdaugherty.milepost.api.data.analysis.SegmentOutlierDetector;
import io.sethdaugherty.milepost.api.data.analysis.SpeedBasedSegmentOutlierDetector;
import io.sethdaugherty.milepost.model.Position;
import io.sethdaugherty.milepost.model.Segment;

import org.assertj.core.util.Lists;
import org.junit.Assert;

public class SegmentServiceTest {

    @Test
    public void testFromPositionList_empty() {
        SegmentService segmentService = new SegmentService(null);
        
        List<Position> positions = new ArrayList<>();
        
        List<Segment> actualSegments = segmentService.fromPositionList(positions);
        
        List<Segment> expectedSegments = new ArrayList<>();
        Assert.assertEquals(expectedSegments, actualSegments);
    }
    
    @Test
    public void testFromPositionList_singleSegment() {
        // No positions should be far enough apart in time to trigger a new segment        
        // TODO: mock the outlier detector
        SegmentService segmentService = new SegmentService(new NoOpOutlierDetector());
        
        List<Position> positions = new ArrayList<>();
        positions.add(createPosition(10000000));
        positions.add(createPosition(10000001));
        positions.add(createPosition(10000002));
        
        List<Segment> actualSegments = segmentService.fromPositionList(positions);
        
        List<Position> expectedPositions = positions;
        List<Segment> expectedSegments = new ArrayList<>();
        expectedSegments.add(new Segment(expectedPositions));
        Assert.assertEquals(expectedSegments, actualSegments);
    }
    
    @Test
    public void testFromPositionList_twoSegments() {
        // The first two positions are far in time from the other two, so they should be split
        // TOOD: mock the outlier detector
        SegmentService segmentService = new SegmentService(new NoOpOutlierDetector());
        
        Position position1 = createPosition(10000000);
        Position position2 = createPosition(10000001);
        Position position3 = createPosition(millisPlusMinutes(10000000, 6));
        Position position4 = createPosition(millisPlusMinutes(10000001, 6));
        List<Position> positions = new ArrayList<>();
        positions.add(position1);
        positions.add(position2);
        positions.add(position3);
        positions.add(position4);

        List<Segment> actualSegments = segmentService.fromPositionList(positions);
        
        List<Segment> expectedSegments = new ArrayList<>();
        expectedSegments.add(new Segment(Lists.newArrayList(position1, position2)));
        expectedSegments.add(new Segment(Lists.newArrayList(position3, position4)));

        Assert.assertEquals(expectedSegments, actualSegments);
    }
    
    private long millisPlusMinutes(int millis, int minutes) {
        // take the number of millis and add the number of minutes to it
        return millis + (minutes * 1000 * 60);
    }

    private Position createPosition(long timestamp) {
        return new Position(timestamp, ThreadLocalRandom.current().nextDouble(-90.0, 90.0), ThreadLocalRandom.current().nextDouble(-180.0, 180.0));
    }
    
    private class NoOpOutlierDetector implements SegmentOutlierDetector {

        @Override
        public Segment removeOutliers(Segment rawSegment) {
            return rawSegment;
        }
        
    }

}
