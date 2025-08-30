package com.project.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/db")
public class DbPageController {

    @GetMapping("/system/postgresql")
    public String postgresqlDbPage() {
        return "dbPage/system/postgresql/list";
    }

    @GetMapping("/system/mongodb")
    public String mongodbDbPage() {
        return "dbPage/system/mongo/index";
    }
}
