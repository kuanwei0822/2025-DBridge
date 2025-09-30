package com.project.service;

import com.project.model.request.TablePageRequest;
import com.project.model.response.TablePageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 取得 Table 資料
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PostgreDataService {

    private final JdbcTemplate jdbcTemplate;

    /**
     * 取得 Table 資料
     */
    public TablePageResult getTableData (TablePageRequest requestBody) {

        // 1. 參數驗證與初始化
        String schema = requestBody.getSchema();
        String tableName = requestBody.getTableName();
        int pageNumber = requestBody.getPageNumber() <= 0 ? 1 : requestBody.getPageNumber();
        int pageSize = requestBody.getPageSize() <= 0 ? 50 : requestBody.getPageSize();

        // 2. 取得 Table Name，並且避免 SQL Injection
        String fullTableName;
        try {
            fullTableName = getQuotedFullTableName(schema, tableName);
        } catch (IllegalArgumentException e) {
            // 如果輸入不合法，應返回錯誤或拋出 RuntimeException
            throw new RuntimeException("Schema 或 Table Name 包含不允許的字元。", e);
        }

        // 3. 獲取總筆數 (Total Count)
        // 由於 fullTableName 已被安全處理，這裡使用 String 拼裝是安全的。
        String countSql = "SELECT COUNT(*) FROM " + fullTableName;
        Long totalCount = jdbcTemplate.queryForObject(countSql, Long.class);

        if (totalCount == null || totalCount == 0) {
            return TablePageResult.builder()
                    .rows(List.of())
                    .totalCount(0)
                    .pageNumber(pageNumber)
                    .pageSize(pageSize)
                    .columnNames(List.of())
                    .build();
        }

        // 4. 獲取當前頁面資料
        long offset = (long) (pageNumber - 1) * pageSize;

        // 5. 構建 ORDER BY 語句，並且避免 SQL Injection
        String orderByClause = buildSafeOrderByClause(requestBody.getOrderByColumn(), requestBody.getSortDirection());

        // 6. 分頁查詢
        String dataSql = String.format(
                "SELECT * FROM %s %s LIMIT %d OFFSET %d",
                fullTableName,
                orderByClause,
                pageSize,
                offset
        );

        // 7. 執行查詢
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(dataSql);

        // 8. 提取欄位名稱 (Metadata)
        List<String> columnNames = List.of();
        if (!rows.isEmpty()) {
            columnNames = rows.get(0).keySet().stream()
                    .collect(Collectors.toList());
        }

        return TablePageResult.builder()
                .rows(rows)
                .totalCount(totalCount)
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .columnNames(columnNames)
                .build();
    }

    /**
     * SQL Injection 防禦方法
     * 獲取經過安全處理和雙引號引用的完整表格名稱 (PostgreSQL 規範)。
     */
    private String getQuotedFullTableName(String schema, String tableName) {

        // 【防禦措施 1: 嚴格輸入驗證】
        // 僅允許字母、數字、底線。這樣可以有效排除惡意的 SQL 分隔符號 (如分號、引號、括號等)。
        // 若需要允許特殊字元，擴充此正則表達式。
        String identifierPattern = "^[a-zA-Z0-9_-]+$";

        if (!schema.matches(identifierPattern) || !tableName.matches(identifierPattern)) {
            throw new IllegalArgumentException("Schema/Table Name: " + schema + "." + tableName + " contains forbidden characters.");
        }

        // 【防禦措施 2: 識別字雙引號引用】
        // 雙引號在 PostgreSQL 中是合法的識別字定界符，確保資料庫將其視為物件名稱，而不是 SQL 命令。
        // 由於內容已經被步驟 1 驗證，這裡的引用是安全的。
        return "\"" + schema + "\"." + "\"" + tableName + "\"";
    }

    /**
     * SQL Injection 防禦方法
     * 安全地構建 ORDER BY 子句，防止 Column Name 欄位的 SQL Injection。
     */
    private String buildSafeOrderByClause(String orderByColumn, String sortDirection) {
        if (orderByColumn == null || orderByColumn.trim().isEmpty()) {
            return "";
        }

        // 【防禦措施 1: 欄位名稱驗證】
        // 使用與 getQuotedFullTableName 相同的嚴格規則來驗證欄位名稱。
        String identifierPattern = "^[a-zA-Z0-9_-]+$";
        if (!orderByColumn.matches(identifierPattern)) {
            throw new IllegalArgumentException("Order by column name contains forbidden characters.");
        }

        // 【防禦措施 2: 引用欄位名稱】
        String quotedColumn = "\"" + orderByColumn + "\"";

        // 【防禦措施 3: 驗證排序方向】
        String safeDirection = "ASC";
        if (sortDirection != null && "DESC".equalsIgnoreCase(sortDirection.trim())) {
            safeDirection = "DESC";
        }

        return " ORDER BY " + quotedColumn + " " + safeDirection;
    }

}
