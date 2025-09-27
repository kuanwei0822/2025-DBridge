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
     * 顯示 PostgreSql Table Metadata 頁面
     */
    @GetMapping("/tableMetadata")
    public String showTableMetadataPage() {
        return "dbPage/system/postgresql/tableMetadata";
    }

    /**
     * 顯示 PostgreSql SQL 執行頁面
     */
    @GetMapping("/sqlExecute")
    public String sqlExecutePage() {
        return "dbPage/system/postgresql/sqlExecute.html";
    }
}
