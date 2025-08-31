package com.project.api.rest;

import com.project.extractor.model.PostgreTableMeta;
import com.project.service.PostgreMetadataExtractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
     * 獲取 PostgreSQL 資料庫中的所有表格的 Metadata。
     */
    @RequestMapping("/tables/metadata")
    public ResponseEntity<List<PostgreTableMeta>> getMetadataTables() {
        List<PostgreTableMeta> tables = postgreMetadataExtractionService.extractTablesMetadata();
        return ResponseEntity.ok(tables);
    }

    /**
     * 獲取 PostgreSQL 資料庫中的所有表格。
     */
    @RequestMapping("/tables")
    public ResponseEntity<List<String>> getTables() {
        List<String> tables = postgreMetadataExtractionService.extractTables();
        return ResponseEntity.ok(tables);
    }
}
