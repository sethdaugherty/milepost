package io.sethdaugherty.milepost.api.data.analysis;

import io.sethdaugherty.milepost.model.Segment;

public interface SegmentOutlierDetector {
    Segment removeOutliers(Segment rawSegment);
}
