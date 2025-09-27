package com.project.model.request;

import lombok.Data;

/**
 * 執行 SQL 的請求參數。
 */
@Data
public class ExecuteSqlRequest {

    private String schema;
    private String sqlStatement;
}
