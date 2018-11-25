package com.beardedtom.db.dao;

import com.beardedtom.db.SensorType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorTypeDAO extends JpaRepository<SensorType, Long> {
    SensorType findBySensorTypeName(String sensorTypeName);
}
