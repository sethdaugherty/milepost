package io.sethdaugherty.milepost.api.data.analysis;

import io.sethdaugherty.milepost.model.Position;
import io.sethdaugherty.milepost.model.Segment;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.filter.*;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.*;

public class KalmanSegmentFilter implements SegmentFilter {

    private static final int THREAD_PRIORITY = 5;

    private static final double DEG_TO_METER = 111225.0;
    private static final double METER_TO_DEG = 1.0 / DEG_TO_METER;

    private static final double TIME_STEP = 1.0;
    private static final double COORDINATE_NOISE = 4.0 * METER_TO_DEG;
    private static final double ALTITUDE_NOISE = 10.0;

    @Override
    public Segment removeOutliers(Segment rawSegment) {
        double accuracy = 100; // default to 100 meters
        double noise = accuracy * METER_TO_DEG;

        List<Position> rawPositionList = rawSegment.getPositionList();

        List<Position> positionList = removeDuplicates(rawPositionList);

        // Initialize the Kalman filter
        Tracker1D latitudeTracker = new Tracker1D(TIME_STEP, COORDINATE_NOISE);
        latitudeTracker.setState(positionList.get(0).getLatitude(), 0.0, noise);
        latitudeTracker.predict(0.0);

        Tracker1D longitudeTracker = new Tracker1D(TIME_STEP, COORDINATE_NOISE);
        longitudeTracker.setState(positionList.get(0).getLongitude(), 0.0, noise);
        longitudeTracker.predict(0.0);


        List<Position> filteredPositions = new ArrayList<>(positionList.size());
        // filter each position
        for (int i=0; i < positionList.size(); i++) {

            Position currentPosition = positionList.get(i);
            accuracy = determineAccuracy(currentPosition);
            noise = accuracy * METER_TO_DEG;

            latitudeTracker.update(currentPosition.getLatitude(), noise);
            latitudeTracker.predict(0.0);

            noise = accuracy * Math.cos(Math.toRadians(currentPosition.getLatitude())) * METER_TO_DEG ;
            longitudeTracker.update(currentPosition.getLongitude(), noise);
            longitudeTracker.predict(0.0);

            double predictedLatitude = latitudeTracker.getPosition();
            double predictedLongitude = longitudeTracker.getPosition();

            filteredPositions.add(new Position(currentPosition.getId(), currentPosition.getTimestamp(), predictedLatitude, predictedLongitude, accuracy));
        }

        return new Segment(filteredPositions);
    }

    private double determineAccuracy(Position currentPosition) {
        if (currentPosition.getAccuracy() == null || currentPosition.getAccuracy() <= 0.01) {
            return 100.0; // default to 100 meters. Don't trust any value of 0. its a default lie
        }
        return currentPosition.getAccuracy();
    }

    private List<Position> removeDuplicates(List<Position> rawPositionList) {
        Map<Long, Position> timestampToPositionsMap = new TreeMap();

        for (Position position : rawPositionList) {
            timestampToPositionsMap.put(position.getTimestamp(), position);
        }

        return new ArrayList<>(timestampToPositionsMap.values());
    }

    //
//    @Override
//    public Segment removeOutliers(Segment rawSegment) {
//        // SETUP FILTER
//        // A = [ 1 ]
//        RealMatrix stateTransitionMatrix = new Array2DRowRealMatrix(new double[] { 1d });
//// no control input
//        RealMatrix controlInputMatrix = null;
//// H = [ 1 ]
//        RealMatrix measurementMatrix = new Array2DRowRealMatrix(new double[] { 1d });
//// Q = [ 0 ]
//        RealMatrix processNoiseCovarianceMatrix = new Array2DRowRealMatrix(new double[] { 0 });
//// R = [ 0 ]
//        RealMatrix measurementNoiseCovarianceMatrix = new Array2DRowRealMatrix(new double[] { COORDINATE_NOISE });
//
//        RealMatrix errorCovarianceMatrix = null;
//
//        ProcessModel pm
//                = new DefaultProcessModel(stateTransitionMatrix, controlInputMatrix, processNoiseCovarianceMatrix, new ArrayRealVector(new double[] { 0 }), errorCovarianceMatrix);
//        MeasurementModel mm = new DefaultMeasurementModel(measurementMatrix, measurementNoiseCovarianceMatrix);
//        KalmanFilter latitudeFilter = new KalmanFilter(pm, mm);
//        KalmanFilter longitudeFilter = new KalmanFilter(pm, mm);
//
//        List<Position> filteredPositionList = new ArrayList<>(rawSegment.getPositionList().size());
//
//        // ITERATE
//        for (int i =0; i < rawSegment.getPositionList().size(); i++) {
//            // predict the state estimate one time-step ahead
//            // optionally provide some control input
//            latitudeFilter.predict();
//
//            // obtain measurement vector z
//            Position currentPosition = rawSegment.getPositionList().get(i);
//            RealVector latitude = new ArrayRealVector(new double[] { currentPosition.getLatitude() });
//            RealVector longitude = new ArrayRealVector(new double[] { currentPosition.getLongitude() });
//
//            // correct the state estimate with the latest measurement
//            latitudeFilter.correct(latitude);
//            longitudeFilter.correct(longitude);
//
//            double[] latitudeEstimate = latitudeFilter.getStateEstimation();
//            double[] longitudeEstimate = longitudeFilter.getStateEstimation();
//            // do something with it
//
//            filteredPositionList.add(new Position(currentPosition.getId(), currentPosition.getTimestamp(), latitudeEstimate[0], longitudeEstimate[0] ));
//
//        }
//
//        return new Segment(filteredPositionList);
//    }
}
