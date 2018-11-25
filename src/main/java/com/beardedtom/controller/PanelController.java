package com.beardedtom.controller;

import com.beardedtom.controller.dto.ControllerUtil;
import com.beardedtom.db.Sensor;
import com.beardedtom.db.User;
import com.beardedtom.db.dao.SensorDAO;
import com.beardedtom.db.dao.SensorTypeDAO;
import com.beardedtom.db.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class PanelC {
    @Autowired
    private SensorDAO sensorDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private ControllerUtil controllerUtil;
    @Autowired
    private SensorTypeDAO sensorTypeDAO;

    @GetMapping("/panel")
    public ModelAndView doGet(@RequestParam(required = false) Boolean loginSuccessful) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("panel");
        if (loginSuccessful != null && loginSuccessful == true) {
            modelAndView.addObject("loginSuccessful", "You've been logged successfully");
        }
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        //create a list of user Sensors
        List<Sensor> sensorList = sensorDAO.findAllByUser(userDAO.findUserByUserEmail(userEmail));
        //List of sensorType for generator
        modelAndView.addObject("listOfSensors", sensorTypeDAO.findAll());
        //check if theres any found, if yes then update SensorStatus happen
        if (sensorList.size() == 0) {
            return modelAndView;
        } else {
            controllerUtil.updateUserSensorStatus(sensorList);
            modelAndView.addObject("sensorsAvailable", sensorList);
        }
        return modelAndView;
    }

    @PostMapping("/panel")
    public ModelAndView doPost(@RequestParam(required = false) String sensorSeed) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("panel");
        User user = userDAO.findUserByUserEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        Sensor sensor = sensorDAO.findBySensorSeedAndUser(sensorSeed, user);

        if (sensor != null) {
            sensorDAO.delete(sensor);
            modelAndView.addObject("deleteSuccessful", "Sensor " + sensor.getSensorUserName() + " has been deleted successfully");
        } else {
            modelAndView.addObject("deleteFailure", "Sensor " + sensor.getSensorUserName() + " has been deleted successfully");

        }
        return modelAndView;
    }
}
