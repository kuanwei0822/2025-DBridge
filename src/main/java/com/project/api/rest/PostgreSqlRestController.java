package com.project.api.rest;

import com.project.extractor.model.PostgreTableMeta;
import com.project.model.request.SchemaRequest;
import com.project.service.PostgreMetadataExtractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    /**
     * 獲取 PostgreSQL 資料庫中的所有 Schema。
     */
    @PostMapping("/schemas")
    public ResponseEntity<List<String>> getSchemas() {
        List<String> schemas = postgreMetadataExtractionService.extractSchemas();
        return ResponseEntity.ok(schemas);
    }

    /**
     * 獲取 PostgreSQL 資料庫中的所有表格。
     */
    @PostMapping("/tables")
    public ResponseEntity<List<String>> getTables(
            @RequestBody SchemaRequest requestBody) {
        List<String> tables = postgreMetadataExtractionService.extractTables(requestBody);
        return ResponseEntity.ok(tables);
    }

    /**
     * 獲取 PostgreSQL 資料庫中的所有表格的 Metadata。
     */
    @PostMapping("/tables/metadata")
    public ResponseEntity<List<PostgreTableMeta>> getMetadataTables(
            @RequestBody SchemaRequest requestBody) {
        List<PostgreTableMeta> tables = postgreMetadataExtractionService.extractTablesMetadata(requestBody);
        return ResponseEntity.ok(tables);
    }
}
