package com.project.model.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class TablePageResult {

    // 實際回傳的資料行列表 (當前頁面的內容)。
    // 每個 Map 包含一筆資料，鍵 (key) 為欄位名稱，值 (value) 為該欄位的值。
    private List<Map<String, Object>> rows;

    // 欄位名稱列表 (用於前端渲染表格的標頭，確保順序正確)。
    private List<String> columnNames;

    // 資料表所有資料的總筆數 (Total Count)。
    private long totalCount;

    // 當前頁碼 (與請求的 pageNumber 一致)。
    private int pageNumber;

    // 每頁顯示的筆數 (與請求的 pageSize 一致)。
    private int pageSize;

}