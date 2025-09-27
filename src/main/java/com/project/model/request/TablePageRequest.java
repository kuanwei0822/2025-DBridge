package com.project.model.request;

import lombok.Data;

@Data
public class TablePageRequest {

    // 資料表所屬的 Schema 名稱
    private String schema;

    // 要查詢的資料表名稱
    private String tableName;

    // 請求的頁碼，從 1 開始。
    private int pageNumber;

    // 每頁顯示的資料筆數 (例如: 25, 50, 100)
    private int pageSize;

    // [選用] 排序欄位：允許前端指定按哪個欄位排序
    private String orderByColumn;

    // [選用] 排序方向："ASC" (升序) 或 "DESC" (降序)
    private String sortDirection;
}