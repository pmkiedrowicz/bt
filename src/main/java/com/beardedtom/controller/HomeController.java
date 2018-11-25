package com.beardedtom.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeC {

    @GetMapping({"/", "/home"})
    public ModelAndView doGet(@RequestParam(required = false) Boolean loginError,
                              @RequestParam(required = false) Boolean logout) {
        ModelAndView modelAndView = new ModelAndView();
        if (loginError != null && loginError == true) {
            modelAndView.addObject("loginError", "Login credentials are wrong.");
        }
        if (logout != null && logout == true) {
            modelAndView.addObject("logoutSuccessfull", "You've been logout successfully.");
        }
        modelAndView.setViewName("home");
        return modelAndView;
    }
}
