package io.sethdaugherty.milepost.api.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.sethdaugherty.milepost.model.Position;

public interface PositionRepository extends JpaRepository<Position, Long> {
	List<Position> findByTimestampBetweenOrderByTimestampAsc(long startTimestamp, long endTimestamp);
}
