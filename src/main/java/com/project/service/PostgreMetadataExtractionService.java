package com.project.service;

import com.project.extractor.PostgreSqlMetadataExtractor;
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

    private final PostgreSqlMetadataExtractor postgreSqlMetadataExtractor;
    private final DataSource dataSource;

    /**
     * 抽取資料庫中所有表格的 Metadata。
     */
    public List<PostgreTableMeta> extractTables() {
        try (Connection conn = dataSource.getConnection()) {
            return postgreSqlMetadataExtractor.extractMetadata(conn);
        } catch (SQLException ex) {
            log.error("提取資料庫 metadata 失敗: {}", ex.getMessage(), ex);
            return Collections.emptyList();
        }
    }
}
