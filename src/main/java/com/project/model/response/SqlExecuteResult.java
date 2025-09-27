package com.project.model.response;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * SQL 執行結果的回應參數。
 */
@Data
public class SqlExecuteResult {
    private String type; // e.g., "SELECT_RESULT", "UPDATE_COUNT", "MESSAGE"
    private String message; // 執行訊息或錯誤訊息
    private Integer count; // 影響的行數 (用於 UPDATE/DELETE)
    private List<Map<String, Object>> rows; // 查詢結果 (用於 SELECT)
}
