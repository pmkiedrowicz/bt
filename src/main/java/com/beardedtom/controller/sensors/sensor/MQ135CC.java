package com.beardedtom.controller.sensors.sensor;

import com.beardedtom.controller.dto.ControllerUtil;
import com.beardedtom.db.Sensor;
import com.beardedtom.db.SensorStatus;
import com.beardedtom.db.dao.SensorDAO;
import com.beardedtom.db.dao.SensorStatusDAO;
import com.beardedtom.db.dao.sensors.MQ135DAO;
import com.beardedtom.db.sensors.MQ135;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MQ135CC {
    private String seedType = "MQ135";

    @PostMapping("/panel/sensors/sensor/mq135")
    public ResponseEntity doPost(@RequestParam String seed, @RequestParam String rzero, @RequestParam String correctedRZero, @RequestParam String resistance, @RequestParam String ppm, @RequestParam String correctedPpm) {
        if (cu.isSeedValid(seed) == false) {
            return ResponseEntity.badRequest().body("Wrong seed");
        } else if (cu.isSeedTypeValid(seed, seedType) == false) {
            return ResponseEntity.badRequest().body("Wrong type of seed");
        } else if (cu.lastPostTimeCheck(seed) == false) {
            return ResponseEntity.status(429).body("POST available only one per minute");
        } else {
            Sensor sensor = sensorDAO.findBySensorSeed(seed);
            MQ135 mq135 = new MQ135();
            mq135.setSensor(sensor);
            if (cu.floatCheck(rzero) &&
                    cu.floatCheck(correctedRZero) &&
                    cu.floatCheck(resistance) &&
                    cu.floatCheck(ppm) &&
                    cu.floatCheck(correctedPpm) == true) {
                mq135.setMq135RZero(Float.parseFloat(rzero));
                mq135.setMq135CorrectedRZero(Float.parseFloat(correctedRZero));
                mq135.setMq135Resistance(Float.parseFloat(resistance));
                mq135.setMq135Ppm(Float.parseFloat(ppm));
                mq135.setMq135CorrectedPpm(Float.parseFloat(correctedPpm));
                mq135.setMq135TimeStamp(System.currentTimeMillis());
                mq135DAO.save(mq135);
                SensorStatus sensorStatus = sensorStatusDAO.findSensorStatusBySensor(sensor);
                sensorStatus.setSensorStatusLastPost(mq135.getMq135TimeStamp());
                sensorStatus.setSensorStatusUpdate(false);
                sensorStatusDAO.save(sensorStatus);
                return ResponseEntity.ok("Data were added to database");
            } else return ResponseEntity.badRequest().body("Wrong values");
        }
    }

    @Autowired
    private ControllerUtil cu;

    @Autowired
    private SensorDAO sensorDAO;

    @Autowired
    private MQ135DAO mq135DAO;

    @Autowired
    private SensorStatusDAO sensorStatusDAO;
}
