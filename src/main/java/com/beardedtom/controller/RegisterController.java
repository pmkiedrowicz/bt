package com.beardedtom.controller;

import com.beardedtom.controller.dto.ControllerUtil;
import com.beardedtom.db.User;
import com.beardedtom.db.dao.UserDAO;
import com.beardedtom.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class RegisterC {
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private ControllerUtil controllerUtil;
    @Autowired
    private SecurityConfig securityConfig;

    @PostMapping("/register")
    public ModelAndView doPost(@Valid User user) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home");
        if (controllerUtil.isNickValid(user.getUserNick()) == false) {
            modelAndView.addObject("errorNick", "Nick must contains lower/upper chars or numbers, length 3-32.");
            return modelAndView;
        } else if (controllerUtil.isEmailValid(user.getUserEmail()) == false) {
            modelAndView.addObject("errorEmail", "Wrong email.");
            return modelAndView;
        } else if (userDAO.findUserByUserEmail(user.getUserEmail()) != null) {
            modelAndView.addObject("errorEmail2", "User with this email already exist.");
            return modelAndView;
        } else if (controllerUtil.isPasswordValid(user.getUserPassword()) == false) {
            modelAndView.addObject("errorPassword", "Password must contains one lower char, one upper char, one special symbol of !@#$%^&*()_+=, length 8-32.");
            return modelAndView;
        } else {
            user.setUserPassword(securityConfig.bCryptPasswordEncoder().encode(user.getUserPassword()));
            user.setUserEnabled(true);
            user.setUserRole("ROLE_USER");
            user.setUserRegisterTime(System.currentTimeMillis());
            userDAO.save(user);
            modelAndView.addObject("registerMessage", user.getUserEmail() + " has been registered successfully");
            return modelAndView;
        }
    }

    @GetMapping("/register")
    public ModelAndView doGet() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home");
        return modelAndView;
    }
}
