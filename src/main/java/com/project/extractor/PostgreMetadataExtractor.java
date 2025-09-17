package com.project.extractor;

import com.project.extractor.model.PostgreTableMeta;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 用於從 PostgreSQL 資料庫中取得 Metadata。
 * - 實作 MetadataExtractor 介面。
 * - 使用 @Profile 註解來指定這個 Component 只在 Profile=postgresql 環境下啟用。
 */
@Slf4j
@Component
public class PostgreMetadataExtractor {

    /**
     * 主方法 :
     * 從資料庫連線中取得所有表格的 Metadata。
     */
    public List<PostgreTableMeta> extractTablesMetadata(Connection connection) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        String catalog = connection.getCatalog();
        String schema = connection.getSchema();

        List<String> tableNames = getTableNames(metaData, catalog, schema);

        List<PostgreTableMeta> postgreTableMetas = new ArrayList<>();
        for (String tableName : tableNames) {
            PostgreTableMeta postgreTableMeta = new PostgreTableMeta();
            postgreTableMeta.setTableName(tableName);
            postgreTableMeta.setRemarks(getTableRemarks(metaData, catalog, schema, tableName));
            postgreTableMeta.setPrimaryKeys(getPrimaryKeys(metaData, catalog, schema, tableName));
            postgreTableMeta.setColumns(getColumns(metaData, catalog, schema, tableName, postgreTableMeta.getPrimaryKeys()));
            postgreTableMeta.setForeignKeys(getForeignKeys(metaData, catalog, schema, tableName));
            postgreTableMeta.setConstraints(getConstraints(connection, schema, tableName)); // custom query
            postgreTableMetas.add(postgreTableMeta);
        }

        return postgreTableMetas;
    }

    /**
     * 主方法 :
     * 從資料庫連線中取得所有表格 List。
     */
    public List<String> extractTables(Connection connection) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        String catalog = connection.getCatalog();
        String schema = connection.getSchema();

        return getTableNames(metaData, catalog, schema);
    }


    /**
     * 獲取指定 catalog(DB) 和 schema 下的所有表格名稱
     */
    private List<String> getTableNames(DatabaseMetaData metaData, String catalog, String schema) throws SQLException {

        List<String> tableNames = new ArrayList<>();
        try (ResultSet rs = metaData.getTables(catalog, schema, "%", new String[]{"TABLE"})) {
            while (rs.next()) {
                tableNames.add(rs.getString("TABLE_NAME"));
            }
        }
        return tableNames;
    }

    /**
     * 獲取指定 Table 的備註資訊。
     */
    private String getTableRemarks(DatabaseMetaData metaData, String catalog, String schema, String tableName) throws SQLException {
        try (ResultSet rs = metaData.getTables(catalog, schema, tableName, new String[]{"TABLE"})) {
            return rs.next() ? rs.getString("REMARKS") : null;
        }
    }

    /**
     * 獲取指定 Table 的所有欄位資訊。
     * 包含欄位名稱、資料類型、大小、是否允許 NULL、預設值、備註等，也會標記哪些欄位是主鍵。
     */
    private List<PostgreTableMeta.ColumnMeta> getColumns(DatabaseMetaData metaData, String catalog, String schema, String tableName, List<String> primaryKeys) throws SQLException {
        List<PostgreTableMeta.ColumnMeta> columns = new ArrayList<>();
        try (ResultSet rs = metaData.getColumns(catalog, schema, tableName, "%")) {
            while (rs.next()) {
                PostgreTableMeta.ColumnMeta col = new PostgreTableMeta.ColumnMeta();
                col.setColumnName(rs.getString("COLUMN_NAME"));
                col.setDataType(rs.getString("TYPE_NAME"));
                col.setColumnSize(rs.getInt("COLUMN_SIZE"));
                col.setNullable(PostgreTableMeta.ColumnMeta.Nullable.from(rs.getInt("NULLABLE")));
                col.setDefaultValue(rs.getString("COLUMN_DEF"));
                col.setRemarks(rs.getString("REMARKS"));
                col.setPrimaryKey(primaryKeys.contains(col.getColumnName()));
                columns.add(col);
            }
        }
        return columns;
    }

    /**
     * 獲取指定 Table 的所有 Primary key 欄位資訊。
     */
    private List<String> getPrimaryKeys(DatabaseMetaData metaData, String catalog, String schema, String tableName) throws SQLException {
        List<String> primaryKeys = new ArrayList<>();
        try (ResultSet rs = metaData.getPrimaryKeys(catalog, schema, tableName)) {
            while (rs.next()) {
                primaryKeys.add(rs.getString("COLUMN_NAME"));
            }
        }
        return primaryKeys;
    }

    /**
     * 獲取指定 Table 的所有 Foreign key 欄位資訊。
     * 包含 Foreign key 名稱、參照的欄位、參照的表格、ON UPDATE 和 ON DELETE 的規則等。
     */
    private List<PostgreTableMeta.ForeignKeyMeta> getForeignKeys(DatabaseMetaData metaData, String catalog, String schema, String tableName) throws SQLException {
        List<PostgreTableMeta.ForeignKeyMeta> foreignKeys = new ArrayList<>();
        try (ResultSet rs = metaData.getImportedKeys(catalog, schema, tableName)) {
            while (rs.next()) {
                PostgreTableMeta.ForeignKeyMeta fk = new PostgreTableMeta.ForeignKeyMeta();
                fk.setFkName(rs.getString("FK_NAME"));
                fk.setColumnName(rs.getString("FKCOLUMN_NAME"));
                fk.setReferencedTable(rs.getString("PKTABLE_NAME"));
                fk.setReferencedColumn(rs.getString("PKCOLUMN_NAME"));
                fk.setUpdateRule(PostgreTableMeta.ForeignKeyMeta.ReferentialRule.from(rs.getShort("UPDATE_RULE")));
                fk.setDeleteRule(PostgreTableMeta.ForeignKeyMeta.ReferentialRule.from(rs.getShort("DELETE_RULE")));
                foreignKeys.add(fk);
            }
        }
        return foreignKeys;
    }

    /**
     * 獲取指定 Table 的所有約束條件資訊。
     * 包含約束名稱、類型、表達式等。
     */
    private List<PostgreTableMeta.ConstraintMeta> getConstraints(Connection conn, String schema, String tableName) throws SQLException {
        List<PostgreTableMeta.ConstraintMeta> constraints = new ArrayList<>();
        String sql = """
        SELECT conname, contype, pg_get_constraintdef(c.oid) AS definition
        FROM pg_constraint c
        JOIN pg_class t ON c.conrelid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE t.relname = ? AND n.nspname = ?
        """;

        try (var ps = conn.prepareStatement(sql)) {
            ps.setString(1, tableName);
            ps.setString(2, schema);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PostgreTableMeta.ConstraintMeta constraint = new PostgreTableMeta.ConstraintMeta();
                    constraint.setConstraintName(rs.getString("conname"));
                    constraint.setConstraintType(PostgreTableMeta.ConstraintMeta.ConstraintType.from(rs.getString("contype")));
                    constraint.setCheckClause(rs.getString("definition")); // 包含表達式
                    constraints.add(constraint);
                }
            }
        }
        return constraints;
    }
}