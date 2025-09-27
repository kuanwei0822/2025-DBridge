package com.project.api.rest;

import com.project.extractor.model.PostgreTableMeta;
import com.project.model.request.ExecuteSqlRequest;
import com.project.model.request.SchemaRequest;
import com.project.model.request.TablePageRequest;
import com.project.model.request.TableRequest;
import com.project.model.response.ApiResponse;
import com.project.model.response.ApiResponseMapper;
import com.project.model.response.SqlExecuteResult;
import com.project.model.response.TablePageResult;
import com.project.service.PostgreDataService;
import com.project.service.PostgreExecuteSqlService;
import com.project.service.PostgreMetadataExtractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * PostgreMetadataController 用於處理 PostgreSQL 相關的 API 請求。
 */
@RestController
@RequestMapping("/api/postgresql")
@RequiredArgsConstructor
public class PostgreSqlRestController {

    private final PostgreMetadataExtractionService postgreMetadataExtractionService;
    private final PostgreExecuteSqlService postgreExecuteSqlService;
    private final PostgreDataService postgreDataService;
    private final ApiResponseMapper apiResponseMapper;

    /**
     * 獲取 PostgreSQL 資料庫中的所有 Schema。
     */
    @PostMapping("/schemas")
    public ApiResponse<List<String>> getSchemas() {
        List<String> schemas = postgreMetadataExtractionService.extractSchemas();
        return apiResponseMapper.success("0", schemas);
    }

    /**
     * 獲取 PostgreSQL 資料庫中的所有表格。
     */
    @PostMapping("/tables")
    public ApiResponse<List<String>> getTables(
            @RequestBody SchemaRequest requestBody) {
        List<String> tables = postgreMetadataExtractionService.extractTables(requestBody);
        return apiResponseMapper.success("0", tables);
    }

    /**
     * 獲取 PostgreSQL 資料庫中的所有表格的 Metadata。
     */
    @PostMapping("/tables/metadata")
    public ApiResponse<List<PostgreTableMeta>> getMetadataTables(
            @RequestBody SchemaRequest requestBody) {
        List<PostgreTableMeta> tables = postgreMetadataExtractionService.extractTablesMetadata(requestBody);
        return apiResponseMapper.success("0", tables);
    }

    /**
     * 獲取 PostgreSQL 資料庫中的單一表格的 Metadata。
     */
    @PostMapping("/table/metadata")
    public ApiResponse<PostgreTableMeta> getMetadataTable(
            @RequestBody TableRequest requestBody) {
        PostgreTableMeta table = postgreMetadataExtractionService.extractTableMetadata(requestBody);
        return apiResponseMapper.success("0", table);
    }

    /**
     * 獲取 PostgreSQL 資料庫中的單一表格的資料。
     */
    @PostMapping("/table/data")
    public ApiResponse<TablePageResult> getTableData(
            @RequestBody TablePageRequest requestBody) {
        TablePageResult result = postgreDataService.getTableData(requestBody);
        return apiResponseMapper.success("0", result);
    }

    /**
     * 執行自訂 PostgreSql SQL
     */
    @PostMapping("/sqlExecute")
    public ApiResponse<SqlExecuteResult> sqlExecute(
            @RequestBody ExecuteSqlRequest requestBody) {
        SqlExecuteResult result = postgreExecuteSqlService.executeSql(requestBody);
        return apiResponseMapper.success("0", result);
    }
}
