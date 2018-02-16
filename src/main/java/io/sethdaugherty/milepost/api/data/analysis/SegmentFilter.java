package io.sethdaugherty.milepost.api.data.analysis;

import io.sethdaugherty.milepost.model.Segment;

public interface SegmentFilter {
    Segment removeOutliers(Segment rawSegment);
}
