package io.sethdaugherty.milepost.api.data;

import org.springframework.data.jpa.repository.JpaRepository;

import io.sethdaugherty.milepost.model.Position;

public interface PositionRepository extends JpaRepository<Position, Long> {

}
