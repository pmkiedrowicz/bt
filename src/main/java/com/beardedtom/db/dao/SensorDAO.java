package com.beardedtom.db.dao;

import com.beardedtom.db.Sensor;
import com.beardedtom.db.SensorStatus;
import com.beardedtom.db.SensorType;
import com.beardedtom.db.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SensorDAO extends JpaRepository<Sensor, Long> {
    List<Sensor> findAllByUser(User user);

    List<Sensor> findAllByUserAndSensorType(User user, SensorType sensorType);

    Sensor findBySensorSeed(String seed);

    Sensor findBySensorSeedAndSensorType(String seed, SensorType sensorType);

    Sensor findBySensorSeedAndUser(String seed, User user);
}
