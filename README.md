# 2025-DBridge
自製 DB Client 程式

## 要求 JAVA 版本

- 17 (OpenJDK or Oracle JDK)

## Security

### 可啟用或關閉登入功能
- 可以在 `application.properties` 設定 `login.active` = `true` 或 `false` 來啟用或關閉登入功能。

### 加入 CSRF 防禦
- 預設啟用 CSRF 防禦。
- 只有通過我方給的頁面帶入 CSRF Token 的 Rest 請求才會被接受。
- 即使惡意頁面在請求中攜帶使用者的 Session Cookie，若沒有 CSRF Token，Server 仍會拒絕該請求。


## 開發筆記

### Docker Compose 設定

- 提供 PostgreSQL 和 MongoDB 服務，用於開發及測試。

### Profile 設定

若需要「系統連線」時可指定 `profile` 參數來指定設定檔。
若不需要「系統連線」，則不需要指定 `profile` 參數。

#### db 設定檔
- `postgresql` 連線 PostgreSQL 用的設定檔。
- `mongo` 連線 MongoDB 用的設定檔。

#### 依據所需「系統連線」設定一個或數個 Profile，例如 :

- IntelliJ IDEA 有 Environment Variables 可以設定。
  ```
  postgresql,mongo
  ```

### 連線驗證器 Validator

- 在 SpringBoot 啟動時，會自動驗證連線設定是否正確。
- 為保持開發彈性，不會因為 DB 連線失敗而啟動失敗，但會印出 Error Log。
