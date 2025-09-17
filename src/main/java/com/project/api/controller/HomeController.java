package com.project.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 首頁
 */
@Controller
@RequestMapping("/home")
public class HomeController {

    @GetMapping
    public String home() {
        return "home/home";
    }
}
