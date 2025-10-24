package com.project.model.request;

import lombok.Data;

@Data
public class MongoQueryRequest {

    // 資料庫名稱
    private String databaseName;
    // 集合名稱
    private String collection;
    // 篩選條件 (JSON 字串)
    // EX: {"price":{"$gte":1000}}
    private String filter;
    // 投影條件 (JSON 字串)
    // EX: {"name":1,"price":1}
    private String projection;
    // 排序條件 (JSON 字串)
    // EX: {"_id":1} / {"price":-1}
    private String sort;
    // 頁碼，預設為 1
    private int page = 1;
    // 每頁筆數，預設為 20
    private int size = 20;
}
