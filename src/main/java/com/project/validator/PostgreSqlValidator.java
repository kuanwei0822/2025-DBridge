package com.project.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * DB 連線驗證器，Application 啟動之後，這邊會立即測試連線，若連線失敗，則會拋出異常，導致應用程式無法啟動。
 * 這樣可以確保應用程式在啟動時就能夠連接到資料庫，避免在後續操作中出現連線問題。
 *
 * - 使用 ApplicationRunner 介面指定 Spring Context 完全啟動之後，執行連線測試。
 */
@Slf4j
@Component
public class PostgreSqlValidator implements ApplicationRunner {

    private final DataSource dataSource;

    public PostgreSqlValidator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            log.info( "DB Connect Success");
        } catch (Exception ex) {
            // 若連線失敗，則記錄錯誤訊息，但不中斷程式。
            log.error("Cannot connect to DB: " + ex.getMessage());
        }
    }
}
