package com.project.service;

import com.project.extractor.PostgreMetadataExtractor;
import com.project.extractor.model.PostgreTableMeta;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * 從資料庫中取出 Metadata 的 Service。
 * - 從 DataSource 取得連線。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PostgreMetadataExtractionService {

    private final PostgreMetadataExtractor postgreMetadataExtractor;
    private final DataSource dataSource;

    /**
     * 列出資料庫中所有表格 List。
     */
    public List<String> extractTables() {
        try (Connection conn = dataSource.getConnection()) {
            return postgreMetadataExtractor.extractTables(conn);
        } catch (SQLException ex) {
            log.error("PostgreMetadataExtractionService extractTables 提取資料庫 Table List 失敗: {}", ex.getMessage(), ex);
            return Collections.emptyList();
        }
    }

    /**
     * 取出資料庫中所有表格的 Metadata。
     */
    public List<PostgreTableMeta> extractTablesMetadata() {
        try (Connection conn = dataSource.getConnection()) {
            return postgreMetadataExtractor.extractTablesMetadata(conn);
        } catch (SQLException ex) {
            log.error("PostgreMetadataExtractionService extractTablesMetadata 提取資料庫 metadata 失敗: {}", ex.getMessage(), ex);
            return Collections.emptyList();
        }
    }
}
