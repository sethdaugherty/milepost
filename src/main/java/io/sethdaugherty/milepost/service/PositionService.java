package io.sethdaugherty.milepost.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.sethdaugherty.milepost.api.data.PositionRepository;
import io.sethdaugherty.milepost.model.Position;
import io.sethdaugherty.milepost.model.Segment;

// TODO: Come up with a better name for this guy
/**
 * Manager that pulls positions out of the database and applies polish to them.
 */
@Component
public class PositionService {
    
    private SegmentService segmentService;
    private PositionRepository positionRepository;

    @Autowired
    public PositionService(PositionRepository repository, SegmentService segmentService) {
        this.positionRepository = repository;
        this.segmentService = segmentService;
    }
    
    public List<Position> findPositionsWithoutOutliers(DateTime startTime, DateTime endTime) {
        List<Position> positions = positionRepository.findByTimestampBetweenOrderByTimestampAsc(startTime.getMillis(), endTime.getMillis());

        List<Segment> segments = segmentService.fromPositionList(positions);
        
        List<Position> cleanedPositions = new ArrayList<>();
        for (Segment segment : segments) {
            cleanedPositions.addAll(segment.getPositionList());
        }        
        return cleanedPositions;
    }
}
