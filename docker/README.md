# 2025-DBridge Docker
建立 DBridge 的 Dev 開發環境

## 開發筆記

#### Docker Compose 注意事項

- DB 的 data 不列入版控，volume 到的資料夾的路徑用 `./DB種類/data`。例如 `./postgres/data`、`/mongo/data`。
- 若要重置資料庫，清除資料庫的資料 + Run init SQL，請刪除 `./DB種類/data` 資料夾。
  (注意：這會清除資料庫的所有資料、權限等設定，全部重置)

#### 建立資料庫

- PostgreSQL
- MongoDB
