package com.project.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * MongoDB 頁面 API
 */
@Slf4j
@Controller
@RequestMapping("/view/mongodb")
@RequiredArgsConstructor
public class MongodbController {

    /**
     * 顯示 MongoDB 查詢頁面
     */
    @GetMapping("/query")
    public String showQueryPage() {
        return "dbPage/system/mongo/query";
    }
}
