package com.project.model.response;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class MongoQueryResult {
    // 總文件筆數 (用於分頁)
    private long totalCount;

    // 查詢結果的列/文件列表。每個 Map 是一個文件，鍵是欄位名，值是欄位值。
    private List<Map<String, Object>> rows;

    // 查詢結果中包含的所有欄位名稱 (用於前端渲染表頭)
    private List<String> columnNames;
}