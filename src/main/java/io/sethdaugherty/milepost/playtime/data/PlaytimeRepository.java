package io.sethdaugherty.milepost.playtime.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.sethdaugherty.milepost.playtime.model.Playtime;

public interface PlaytimeRepository extends JpaRepository<Playtime, Long> {
	List<Playtime> findAllByDate(String date);
}
