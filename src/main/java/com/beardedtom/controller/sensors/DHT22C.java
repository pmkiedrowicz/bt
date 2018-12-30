package com.beardedtom.controller.sensors;

import com.beardedtom.controller.dto.ControllerUtil;
import com.beardedtom.controller.sensors.sensor.DHT22CC;
import com.beardedtom.db.Sensor;
import com.beardedtom.db.dao.SensorDAO;
import com.beardedtom.db.dao.SensorTypeDAO;
import com.beardedtom.db.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class DHT22C {
    @GetMapping("/panel/sensors/dht22")
    public ModelAndView doGet() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("panel/sensors/dht22");
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        //create a list of user DHT22 Sensors
        List<Sensor> sensorList = sensorDAO.findAllByUserAndSensorType(userDAO.findUserByUserEmail(userEmail), sensorTypeDAO.findBySensorTypeName("DHT22"));
        //check if theres any found, if yes then update SensorStatus happen
        if (sensorList.size() == 0) {
            return modelAndView;
        } else if (sensorList.size() > 0) {
            controllerUtil.updateUserSensorStatus(sensorList);
            modelAndView.addObject("listOfSensors", sensorList);
            for (int i = 0; i < sensorList.size(); i++) {
                modelAndView.addObject("s" + i, controllerUtil.getLast24hListOfDHT22(sensorList.get(i)));
            }
        }
        return modelAndView;
    }

    @Autowired
    private SensorDAO sensorDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private SensorTypeDAO sensorTypeDAO;
    @Autowired
    private ControllerUtil controllerUtil;

}
