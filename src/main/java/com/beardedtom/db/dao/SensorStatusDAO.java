package com.beardedtom.db.dao;

import com.beardedtom.db.Sensor;
import com.beardedtom.db.SensorStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorStatusDAO extends JpaRepository<SensorStatus,Long> {
    SensorStatus findSensorStatusBySensor(Sensor sensor);
}
