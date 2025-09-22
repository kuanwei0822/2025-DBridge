package com.project.model.request;

import lombok.Data;

/**
 * 資料庫 table 的請求參數。
 */
@Data
public class TableRequest {

    private String schema;
    private String tableName;
}
