package io.sethdaugherty.milepost.api.data.analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.sethdaugherty.milepost.metrics.TimeExecutionProfiler;
import io.sethdaugherty.milepost.model.Position;
import io.sethdaugherty.milepost.model.Segment;

@Component
public class SpeedBasedSegmentOutlierDetector implements SegmentOutlierDetector {

    private static final Logger logger = LoggerFactory.getLogger(SpeedBasedSegmentOutlierDetector.class);

    private static final double Z_SCORE_THRESHOLD = 2.5;

    @Override
    public Segment removeOutliers(Segment rawSegment) {
        List<Position> cleanedPositions = new ArrayList<>();
        
        List<Position> rawPositions = rawSegment.getPositionList();
        
        // Compute the euclidian distance between each pair of points (p1, p2) and collect it in a map
        // Note: the first Position always has a distance of 0;
        SortedMap<Position, Double> positionToSpeedMap = computeSpeed(rawPositions);
        
        SummaryStatistics summaryStats = new SummaryStatistics();
        for (Double speed : positionToSpeedMap.values()) {
            summaryStats.addValue(speed);
        }
        
        double standardDeviation = summaryStats.getStandardDeviation();
        double mean = summaryStats.getMean();
        
        logger.debug("Computed mean " + mean + " and standard deviation " + standardDeviation);
        logger.debug("rawSegment size: " + rawPositions.size() + " positionToSpeedMap size: " + positionToSpeedMap.size() + " summaryStats: max " + summaryStats.getMax() + " min:" + summaryStats.getMin() + " n:" + summaryStats.getN());
        for (Position position : positionToSpeedMap.keySet()) {
            Double speed = positionToSpeedMap.get(position);
            double zScore = (speed - mean) / standardDeviation;
            logger.debug("Computed zScore: " + zScore + " speed: " + speed + " mean: " + mean + " stdDev: " + standardDeviation);
            if (Math.abs(zScore) < Z_SCORE_THRESHOLD) {
                cleanedPositions.add(position);
            }
        }
        
        return new Segment(cleanedPositions);
    }

    private SortedMap<Position, Double> computeSpeed(List<Position> rawPositions) {
        EuclideanDistance euclideanDistance = new EuclideanDistance();
        SortedMap<Position, Double> positionToSpeedMap = new TreeMap<>();

        if (rawPositions.size() > 0) {
            positionToSpeedMap.put(rawPositions.get(0), 0.0);
        }

        for (int i = 1; i < rawPositions.size(); i++) {
            Position currentPosition = rawPositions.get(i);

            // Get the mean of the nearest 3 points on either side
            int count = 0;
            double sum = 0;
            List<Double> speeds = new ArrayList<>();
            for (int j = i - 3; j <= i + 3; j++) {
                if ((j < 0) || j == i || j >= rawPositions.size()) {
                    //continue;
                }
                else {
                    Position previousPosition = rawPositions.get(j);
    
                    if (previousPosition.getTimestamp() == currentPosition.getTimestamp()) {
                        logger.debug("Found the same position? i:" + i + " j: " + j);
                        continue;
                    }
                    
                    double[] v1 = { previousPosition.getLatitude(), previousPosition.getLongitude() };
                    double[] v2 = { currentPosition.getLatitude(), currentPosition.getLongitude() };
    
                    double distance = euclideanDistance.compute(v1, v2);
                    double speed = distance / (currentPosition.getTimestamp() - previousPosition.getTimestamp());
                    speed = Math.abs(speed);
                    speeds.add(speed);
                    
                    logger.debug("distance: " + distance + " speed: " + speed + " " + currentPosition.getTimestamp() + " " + previousPosition.getTimestamp());
                    sum += speed;
                    count++;
                }
            }

            logger.debug("i: " + i);
            if (speeds.size() > 0) {
                Collections.sort(speeds);
                double median = speeds.get( speeds.size()/2 );
                positionToSpeedMap.put(currentPosition, median);
            }
            else {
                logger.debug("No speed found for i: " + i);
                positionToSpeedMap.put(currentPosition, 0.0);
            }
            //double mean = sum / count;

            //positionToSpeedMap.put(currentPosition, mean);
        }

        return positionToSpeedMap;
    }
}
