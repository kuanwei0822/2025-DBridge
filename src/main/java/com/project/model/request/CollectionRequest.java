package com.project.model.request;

import lombok.Data;

/**
 * 取得 MongoDB 資料庫 collections 的請求參數。
 */
@Data
public class CollectionRequest {

    // 資料庫名稱
    private String databaseName;
}
