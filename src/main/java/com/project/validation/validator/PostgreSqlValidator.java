package com.project.validation.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * PostgreSQL 連線驗證器，測試連線，若連線失敗，則會 Return False。
 *
 * 實作 PostgreSql 介面 :
 * - 指定這個 Validator 驗證器的類型
 * - 執行驗證邏輯，測試連線是否成功。
 */
@Slf4j
@Component
public class PostgreSqlValidator implements Validator {

    private final DataSource dataSource;

    public PostgreSqlValidator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // 回傳 驗證的類型
    @Override
    public ValidatorTarget target() {
        return ValidatorTarget.POSTGRESQL;
    }

    // 執行 驗證邏輯
    @Override
    public boolean validate() {
        try (Connection conn = dataSource.getConnection()) {
            log.info( "PostgreSqlValidator : DB Connect Success");
            return true;
        } catch (Exception ex) {
            // 若連線失敗，則記錄錯誤訊息，但不中斷程式。
            log.error("PostgreSqlValidator : Cannot connect to DB: " + ex.getMessage());
            return false;
        }
    }
}
