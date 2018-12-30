package com.beardedtom.controller.dto;

import com.beardedtom.db.Sensor;
import com.beardedtom.db.SensorStatus;
import com.beardedtom.db.dao.SensorDAO;
import com.beardedtom.db.dao.SensorStatusDAO;
import com.beardedtom.db.dao.sensors.DHT22DAO;
import com.beardedtom.db.sensors.DHT22;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Component
public class ControllerUtil {
    @Autowired
    private SensorStatusDAO sensorStatusDAO;
    @Autowired
    private SensorDAO sensorDAO;
    @Autowired
    private DHT22DAO dht22DAO;

    public Boolean isNickValid(String nick) {
        /**
         * Nick must contains:
         * char like a-zA-Z0-9
         * length 3-32
         */
        if (nick == null) {
            return false;
        }
        Pattern patt = Pattern.compile("^[a-zA-Z0-9]{3,32}$");
        return patt.matcher(nick).matches();
    }

    public Boolean isPasswordValid(String password) {
        /**
         * Password must contains:
         * one lowercase char
         * one uppercase char
         * one special symbol
         * length 8-32
         */
        if (password == null) {
            return false;
        }
        Pattern patt = Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[^\\w\\s]).{8,32}$");
        return patt.matcher(password).matches();
    }

    public Boolean isEmailValid(String email) {
        /**
         * Email must be followed by:
         * RFC5322
         */
        if (email == null) {
            return false;
        }
        Pattern patt = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-" +
                "9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-" +
                "\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-" +
                "\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-" +
                "9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-" +
                "9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-" +
                "9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-" +
                "\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-" +
                "\\x7f])+)\\])");
        return patt.matcher(email).matches();
    }

    public void updateUserSensorStatus(List<Sensor> sensorList) {
        /**
         * Update sensors status on list of user sensors.
         */
        SensorStatus sensorStatus;
        for (Sensor sensor : sensorList) {
            sensorStatus = sensorStatusDAO.findSensorStatusBySensor(sensor);
            if (sensorStatus.getSensorStatusLastPost() + 300000 > System.currentTimeMillis()) {
                sensorStatus.setSensorStatusUpdate(true);
                sensorStatusDAO.save(sensorStatus);
            } else {
                sensorStatus.setSensorStatusUpdate(false);
                sensorStatusDAO.save(sensorStatus);
            }
        }
    }

    public String generateRandomSeed() {
        /**
         * Generate random seed with UUID library.
         */
        UUID random = UUID.randomUUID();
        String seed = random.toString();
        return seed;
    }

    public Boolean checkForExistingSeed(String seed) {
        /**
         * Check for existing SensorSeed. If found method returns true, otherwise return false.
         */
        if (sensorDAO.findBySensorSeed(seed) != null) {
            return true;
        } else return false;
    }

    public String generateSensorSeed() {
        /**
         * Generate sensor Seed. Also check for avability. Returns String seed.
         */
        String seed = generateRandomSeed();
        if (checkForExistingSeed(seed) == true) {
            seed = generateRandomSeed();
        }
        return seed;
    }

    public Boolean isSeedValid(String seed) {
        /**
         * Check if seed is available in database, if not return false
         */
        if (sensorDAO.findBySensorSeed(seed) != null) {
            return true;
        } else return false;
    }

    public Boolean isSeedTypeValid(String seed, String seedType) {
        /**
         * Check if seedType is in pair with seed, if not return false
         */
        Sensor sensor = sensorDAO.findBySensorSeed(seed);
        if (sensor.getSensorType().getSensorTypeName().equals(seedType) == true) {
            return true;
        } else return false;
    }

    public boolean lastPostTimeCheck(String seed) {
        /**
         * Check if lastPost is in atleast 60s ago, if not return false
         */
        if (sensorDAO.findBySensorSeed(seed).getSensorStatus().getSensorStatusLastPost() + 60000 < System.currentTimeMillis()) {
            return true;
        } else return false;
    }

    public Object getLast24hListOfDHT22(Sensor sensor) {
        /**
         * Get last 24h values of DHT22 sensor
         */
        if (sensor == null) {
            return null;
        } else {
            long last24h = 86400000;
            long currentTime = System.currentTimeMillis();
            List finalArr = new ArrayList<>();
            List<DHT22> allBySensor = dht22DAO.findAllBySensor(sensor);
            List<DHT22> last24BySensor = new ArrayList<>();

            for (DHT22 dht22 : allBySensor) {
                if (dht22.getDht22TimeStamp() >= currentTime - last24h) {
                    last24BySensor.add(dht22);
                }
            }
            finalArr.add(sensor.getSensorUserName());
            for (DHT22 el : last24BySensor) {
                finalArr.add(el.getDht22TimeStamp());
                finalArr.add(el.getDht22Temperature());
                finalArr.add(el.getDht22Humidity());
            }
            return finalArr;
        }
    }

    public boolean floatCheck(String number) {
        /**
         * Check if present number parseable to Float type
         */
        try {
            Float.parseFloat(number);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public boolean humidityRangeCheck(String humidity, int minimum, int maximum) {
        /**
         * Check if given humidity is in range of minimum and maximum values
         */
        try {
            Float humi = Float.parseFloat(humidity);
            if (humi < minimum || humi > maximum) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public boolean temperatureRangeCheck(String temperature, int minimum, int maximum) {
        /**
         * Check if given temperature is in range of minimum and maximum values
         */
        try {
            Float temp = Float.parseFloat(temperature);
            if (temp <= minimum || temp >= maximum) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
