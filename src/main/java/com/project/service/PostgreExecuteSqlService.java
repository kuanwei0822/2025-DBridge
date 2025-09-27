package com.project.service;

import com.project.model.request.ExecuteSqlRequest;
import com.project.model.response.SqlExecuteResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * 執行 SQL 的 Service。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PostgreExecuteSqlService {

    private final JdbcTemplate jdbcTemplate;

    /**
     * 根據 Search Path 設定，執行使用者提供的 SQL 語句。
     */
    public SqlExecuteResult executeSql(ExecuteSqlRequest requestBody) {

        String schema = requestBody.getSchema();
        String sql = requestBody.getSqlStatement().trim();

        // 檢查輸入
        if (schema == null || schema.isEmpty()) {
            return buildErrorResult("Schema 參數不得為空。", "SCHEMA_MISSING");
        }
        if (sql.isEmpty()) {
            return buildErrorResult("SQL 語句不得為空。", "SQL_MISSING");
        }

        log.info("執行 SQL 請求: Schema={}, SQL={}", schema, sql);

        try {
            // 使用 execute() 方法確保我們可以訪問底層 Connection
            return jdbcTemplate.execute((ConnectionCallback<SqlExecuteResult>) conn -> {

                // 1. 設定 Search Path
                String setSearchPathSql = "SET search_path TO \"" + schema + "\", public;";

                // 2. 執行設定 search_path 的 SQL
                try (Statement pathStmt = conn.createStatement()) {
                    pathStmt.execute(setSearchPathSql);
                    log.debug("成功設定 search_path: {}", setSearchPathSql);
                } catch (SQLException e) {
                    log.error("設定 search_path 失敗: {}", setSearchPathSql, e);
                    // 若設定失敗，直接回傳錯誤結果
                    return buildErrorResult("設定 Schema 搜尋路徑失敗。", "SEARCH_PATH_ERROR");
                }

                // 2. 執行使用者 SQL
                return executeUserSql(conn, sql);
            });
        } catch (DataAccessException e) {
            // 處理 Spring JDBC 相關的例外 (例如 SQL語法錯誤, 找不到表)
            String errorMessage = "SQL 執行失敗: " + (e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
            log.error(errorMessage, e);
            return buildErrorResult(errorMessage, "SQL_EXECUTION_ERROR");
        }
    }

    /**
     * 處理 SELECT 和非 SELECT 語句的執行。
     */
    private SqlExecuteResult executeUserSql(Connection con, String sql) throws SQLException {

        try (Statement stmt = con.createStatement()) {

            boolean isResultSet = stmt.execute(sql); // 執行 SQL

            if (isResultSet) {
                // 處理 SELECT 語句 (回傳 ResultSet)
                try (ResultSet rs = stmt.getResultSet()) {
                    return processSelectResult(rs);
                }
            } else {
                // 處理 DML/DDL 語句 (回傳影響的行數)
                int updateCount = stmt.getUpdateCount();
                return buildUpdateResult(updateCount);
            }
        }
    }

    /**
     * 將 ResultSet 轉換為 List<Map<String, Object>>
     */
    private SqlExecuteResult processSelectResult(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData(); // 從結果取得 MetaData
        int columnCount = metaData.getColumnCount(); // 取得欄位數
        List<Map<String, Object>> rows = new ArrayList<>();

        // 逐行讀取結果，並將每行轉換為 Map
        while (rs.next()) {
            Map<String, Object> row = new LinkedHashMap<>(); // 使用 LinkedHashMap 保持欄位順序
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnLabel(i); // 使用 getColumnLabel 取得別名
                row.put(columnName, rs.getObject(i));
            }
            rows.add(row);
        }

        SqlExecuteResult result = new SqlExecuteResult();
        result.setType("SELECT_RESULT");
        result.setMessage("查詢成功。");
        result.setRows(rows);
        return result;
    }

    /**
     *  建立更新結果
     */
    private SqlExecuteResult buildUpdateResult(int count) {
        SqlExecuteResult result = new SqlExecuteResult();
        result.setType("UPDATE_COUNT");
        result.setMessage("執行成功，影響 " + count + " 筆資料。");
        result.setCount(count);
        return result;
    }

    /**
     *  建立錯誤結果
     */
    private SqlExecuteResult buildErrorResult(String message, String type) {
        SqlExecuteResult result = new SqlExecuteResult();
        result.setType(type);
        result.setMessage(message);
        return result;
    }
}
