package com.project.validation;

import com.project.validation.validator.ValidatorTarget;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.sql.Connection;

/**
 * DB 連線驗證器，Application 啟動之後，這邊會立即測試連線，若連線失敗，則會拋出異常，導致應用程式無法啟動。
 * 這樣可以確保應用程式在啟動時就能夠連接到資料庫，避免在後續操作中出現連線問題。
 *
 * - 使用 ApplicationRunner 介面指定 Spring Context 完全啟動之後，執行連線測試。
 */
@Slf4j
@Component
public class ValidationOnStartup implements ApplicationRunner {

    private final ValidationCheck validationCheck;

    public ValidationOnStartup(ValidationCheck validationCheck) {
        this.validationCheck = validationCheck;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        // 在應用程式啟動時，立即測試 PostgreSQL 連線。
        boolean validation = validationCheck.check(ValidatorTarget.POSTGRESQL);
        // 若連線失敗，則記錄錯誤訊息，但不中斷程式。
        if (!validation) {
            log.error("ValidationOnStartup : PostgreSQL connection failed.");
        } else {
            log.info("ValidationOnStartup : PostgreSQL connection successful.");
        }

    }
}
