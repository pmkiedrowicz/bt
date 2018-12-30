package com.beardedtom.controller.sensors.sensor;

import com.beardedtom.controller.dto.ControllerUtil;
import com.beardedtom.controller.sensors.sensor.interfaces.DHT22CCI;
import com.beardedtom.db.Sensor;
import com.beardedtom.db.SensorStatus;
import com.beardedtom.db.dao.SensorDAO;
import com.beardedtom.db.dao.SensorStatusDAO;
import com.beardedtom.db.dao.sensors.DHT22DAO;
import com.beardedtom.db.sensors.DHT22;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DHT22CC implements DHT22CCI {

    @PostMapping("/panel/sensors/sensor/dht22")
    public ResponseEntity doPost(@RequestParam String seed, @RequestParam String temperature, @RequestParam String humidity) {
        if (cu.isSeedValid(seed) == false) {
            return ResponseEntity.badRequest().body("Wrong seed");
        } else if (cu.isSeedTypeValid(seed, seedType) == false) {
            return ResponseEntity.badRequest().body("Wrong type of seed");
        } else if (cu.lastPostTimeCheck(seed) == false) {
            return ResponseEntity.status(429).body("POST available only one per minute");
        } else {
            Sensor sensor = sensorDAO.findBySensorSeed(seed);
            DHT22 dht22 = new DHT22();
            dht22.setSensor(sensor);
            if (cu.floatCheck(temperature) &&
                    cu.floatCheck(humidity) &&
                    cu.humidityRangeCheck(humidity, humiMin, humiMax) &&
                    cu.temperatureRangeCheck(temperature, tempMin, tempMax) == true) {
                dht22.setDht22Temperature(Float.parseFloat(temperature));
                dht22.setDht22Humidity(Float.parseFloat(humidity));
                dht22.setDht22TimeStamp(System.currentTimeMillis());
                dht22DAO.save(dht22);
                SensorStatus sensorStatus = sensorStatusDAO.findSensorStatusBySensor(sensor);
                sensorStatus.setSensorStatusLastPost(dht22.getDht22TimeStamp());
                sensorStatus.setSensorStatusUpdate(false);
                sensorStatusDAO.save(sensorStatus);
                return ResponseEntity.ok("Data were added to database");
            } else return ResponseEntity.badRequest().body("Wrong values");
        }
    }

    @Autowired
    private DHT22DAO dht22DAO;

    @Autowired
    private SensorDAO sensorDAO;

    @Autowired
    private ControllerUtil cu;
    @Autowired
    private SensorStatusDAO sensorStatusDAO;
}
