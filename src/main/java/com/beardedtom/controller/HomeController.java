package com.beardedtom.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    @GetMapping({"/", "/home"})
    public ModelAndView doGet(@RequestParam(required = false) Boolean loginFailure,
                              @RequestParam(required = false) Boolean logoutSuccessful) {
        ModelAndView modelAndView = new ModelAndView();
        if (loginFailure != null && loginFailure == true) {
            modelAndView.addObject("loginFailure", "Login credentials are wrong.");
        }
        if (logoutSuccessful != null && logoutSuccessful == true) {
            modelAndView.addObject("logoutSuccessful", "You've been logout successfully.");
        }
        modelAndView.setViewName("home");
        return modelAndView;
    }
}
