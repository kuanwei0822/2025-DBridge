# 2025-DBridge
自製 DB Client 程式

## 開發筆記

### JAVA 版本

- 17 (OpenJDK or Oracle JDK)

### 連線驗證器 Validator

- 在 SpringBoot 啟動時，會自動驗證連線設定是否正確。
- 為保持開發彈性，不會因為 DB 連線失敗而啟動失敗，但會印出 Error Log。
