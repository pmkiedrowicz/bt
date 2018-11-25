package com.beardedtom.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AccessDeniedController {

    private ModelAndView modelAndView = new ModelAndView();

    @GetMapping
    public ModelAndView doGet() {
        modelAndView.setViewName("access_denied");
        return modelAndView;
    }
}
