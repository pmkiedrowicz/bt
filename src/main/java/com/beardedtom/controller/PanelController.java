package com.beardedtom.controller;

import com.beardedtom.controller.dto.ControllerUtil;
import com.beardedtom.db.Sensor;
import com.beardedtom.db.SensorStatus;
import com.beardedtom.db.SensorType;
import com.beardedtom.db.User;
import com.beardedtom.db.dao.SensorDAO;
import com.beardedtom.db.dao.SensorStatusDAO;
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
public class PanelController {
    @Autowired
    private SensorDAO sensorDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private ControllerUtil controllerUtil;
    @Autowired
    private SensorTypeDAO sensorTypeDAO;
    @Autowired
    private SensorStatusDAO sensorStatusDAO;

    @GetMapping("/panel")
    public ModelAndView doGet(@RequestParam(required = false) Boolean loginSuccessful,
                              @RequestParam(required = false) String deleteSuccessful,
                              @RequestParam(required = false) String sensorGenerateFailure,
                              @RequestParam(required = false) String sensorGenerateSizeFailure,
                              @RequestParam(required = false) String sensorGenerateSuccess) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("panel");
        if (loginSuccessful != null && loginSuccessful == true) {
            modelAndView.addObject("loginSuccessful", "You've been logged successfully");
        }

        if (deleteSuccessful != null) {
            modelAndView.addObject("deleteSuccessful", "Sensor " + deleteSuccessful + " has been deleted successful");
        }

        if (sensorGenerateFailure != null) {
            modelAndView.addObject("sensorGenerateFailure", "Wrong params");
        }

        if (sensorGenerateSizeFailure != null) {
            modelAndView.addObject("sensorGenerateFailure", "You've reached maximum of enabled " + sensorGenerateSizeFailure + " sensors");
        }

        if (sensorGenerateSuccess != null) {
            modelAndView.addObject("sensorGenerateSuccess", "Sensor " + sensorGenerateSuccess + " has been added successful");
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
    public ModelAndView doPost(@RequestParam(required = false) String sensorSeed,
                               @RequestParam(required = false) String sensorTypeName,
                               @RequestParam(required = false) String sensorName) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/panel");
        User user = userDAO.findUserByUserEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        Sensor sensor = sensorDAO.findBySensorSeedAndUser(sensorSeed, user);

        if (sensor != null) {
            sensorDAO.delete(sensor);
            modelAndView.addObject("deleteSuccessful", sensor.getSensorUserName());
            return modelAndView;
        } else {
            if (sensorTypeName == null || sensorName == null) {
                modelAndView.addObject("sensorGenerateFailure", "Wrong params");
                return modelAndView;
            } else {
                SensorType sensorT = sensorTypeDAO.findBySensorTypeName(sensorTypeName);
                if (sensorDAO.findAllByUserAndSensorType(user, sensorT).size() > 4) {
                    modelAndView.addObject("sensorGenerateSizeFailure", sensorTypeName);
                    return modelAndView;
                } else {
                    SensorStatus sensorStatus = new SensorStatus();
                    sensorStatus.setSensorStatusUpdate(false);
                    sensorStatus.setSensorStatusLastPost(0l);
                    SensorStatus sensorStatus1 = sensorStatusDAO.save(sensorStatus);
                    SensorType sensorType = sensorTypeDAO.findBySensorTypeName(sensorTypeName);
                    Sensor s = new Sensor();
                    s.setSensorType(sensorType);
                    s.setSensorUserName(sensorName);
                    s.setSensorRegisterTime(System.currentTimeMillis());
                    String seed = controllerUtil.generateSensorSeed();
                    s.setSensorSeed(seed);
                    s.setUser(user);
                    s.setSensorStatus(sensorStatus1);
                    sensorDAO.save(s);
                    modelAndView.addObject("sensorGenerateSuccess", s.getSensorUserName());
                    return modelAndView;
                }
            }
        }
//        modelAndView.setViewName("panel");
//        return modelAndView;
    }
}
