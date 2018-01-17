package io.sethdaugherty.milepost.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.sethdaugherty.milepost.api.data.analysis.SegmentOutlierDetector;
import io.sethdaugherty.milepost.model.Position;
import io.sethdaugherty.milepost.model.Segment;

@Component
/**
 * Service that can create Segments from a raw list of Positions
 */
public class SegmentService {

    private SegmentOutlierDetector outlierDetector;
    
    private static final long TIME_SEGMENTATION_THRESHOLD_MILLIS = 300000; // 5 min

    @Autowired
    public SegmentService(SegmentOutlierDetector outlierDetector) {
        this.outlierDetector = outlierDetector;
    }
    
    /**
     * The current (naive, incomplete) implementation is to split into segments based on the time delta
     * between points. If the delta is above the segmentation threshold, a segment will be created.
     */
    public List<Segment> fromPositionList(List<Position> positions) {
        List<Segment> segments = new ArrayList<Segment>();
        
        List<Position> currentSegmentPositions = new ArrayList<>();
        
        Position previousPosition = null;
        for (Position currentPosition : positions) {
            if (previousPosition == null) {
                previousPosition = currentPosition;
                currentSegmentPositions.add(currentPosition);
                continue;
            }
            
            long timeDelta = currentPosition.getTimestamp() - previousPosition.getTimestamp();
            
            if (timeDelta >= TIME_SEGMENTATION_THRESHOLD_MILLIS) {
                segments.add(new Segment(currentSegmentPositions));
                currentSegmentPositions = new ArrayList<>();
            }
            
            currentSegmentPositions.add(currentPosition);
            previousPosition = currentPosition;
        }
        
        if (!currentSegmentPositions.isEmpty()) {
            segments.add(new Segment(currentSegmentPositions));
        }
        
        
        List<Segment> cleanedSegments = new ArrayList<>();
        
        for (Segment segment : segments) {
            cleanedSegments.add(outlierDetector.removeOutliers(segment));
        }
        return cleanedSegments;
    }
}
