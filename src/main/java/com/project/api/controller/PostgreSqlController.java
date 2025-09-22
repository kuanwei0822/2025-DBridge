package com.project.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * PostgreSql 頁面 API
 */
@Slf4j
@Controller
@RequestMapping("/view/postgresql")
@RequiredArgsConstructor
public class PostgreSqlController {

    /**
     * 顯示 PostgreSql Table 頁面
     */
    @GetMapping("/tableList")
    public String showTableListPage() {
        return "dbPage/system/postgresql/tableList";
    }
    /**
     * 顯示 PostgreSql Table 資料內容頁面
     */
    @GetMapping("/tableMetadata")
    public String showTableMetadataPage() {
        return "dbPage/system/postgresql/tableMetadata";
    }
}
