package com.beardedtom.db.dao.sensors;

import com.beardedtom.db.Sensor;
import com.beardedtom.db.sensors.DHT22;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DHT22DAO extends JpaRepository<DHT22, Long> {
    List<DHT22> findAllBySensor(Sensor sensor);

    List<DHT22> findAllBySensorOrderByDht22Id(Sensor sensor);

    DHT22 getFirstBySensorAndDht22TimeStampAfter(Sensor sensor, long timestamp);

    DHT22 getFirstBySensorAndDht22TimeStampGreaterThan(Sensor sensor, long timestamp);

    DHT22 getFirstBySensorAndDht22TimeStampIsAfter(Sensor sensor, long timestamp);

    DHT22 findFirstBySensorAndDht22TimeStampBetween(Sensor sensor, long min, long max);
}
