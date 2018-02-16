package io.sethdaugherty.milepost.configuration;

import io.sethdaugherty.milepost.api.data.analysis.SegmentFilter;
import io.sethdaugherty.milepost.api.data.analysis.KalmanSegmentFilter;
import io.sethdaugherty.milepost.service.SegmentService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MilePostConfiguration {

    @Bean
    public SegmentFilter segmentFilter() {
        return new KalmanSegmentFilter();
    }

    @Bean
    public SegmentService segmentService() {
        return new SegmentService(segmentFilter());
    }
}
