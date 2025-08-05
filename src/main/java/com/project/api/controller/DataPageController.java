package com.project.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 顯示特定 table 的實際資料內容（前100筆）
 */
@Slf4j
@Controller
@RequestMapping("/data")
@RequiredArgsConstructor
public class DataPageController {

    private final JdbcTemplate jdbcTemplate;

    @GetMapping("/table")
    public String showTableData(@RequestParam("name") String tableName, Model model) {
        try {
                String sql = "SELECT * FROM " + tableName + " limit 5000;";
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

            List<String> columnNames = new ArrayList<>();
            if (!rows.isEmpty()) {
                columnNames.addAll(rows.get(0).keySet());
            }

            model.addAttribute("tableName", tableName);
            model.addAttribute("columnNames", columnNames);
            model.addAttribute("rows", rows);
            return "data/table";
        } catch (Exception e) {
            log.error("讀取資料表 {} 時發生錯誤：{}", tableName, e.getMessage(), e);
            model.addAttribute("errorMessage", "讀取資料失敗：" + e.getMessage());
            return "data/table";
        }
    }
}
