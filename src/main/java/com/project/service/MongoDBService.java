package com.project.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.project.model.request.CollectionRequest;
import com.project.model.request.MongoQueryRequest;
import com.project.model.response.MongoQueryResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.FindIterable;
import org.bson.Document;

import java.util.*;

/**
 * 用於處理 MongoDB 相關的服務邏輯。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MongoDBService {

    private final MongoClient mongoClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 獲取 MongoDB 資料庫中的所有 DB。
     */
    public List<String> getDatabases() {
        return mongoClient.listDatabaseNames().into(new ArrayList<>());
    }

    /**
     * 獲取指定 Database 中的所有 Collections。
     */
    public List<String> getCollections(CollectionRequest requestBody) {
        MongoDatabase database = mongoClient.getDatabase(requestBody.getDatabaseName());
        List<String> collections = new ArrayList<>();
        for (String n : database.listCollectionNames()) collections.add(n);
        return collections;
    }

    /**
     * 遞迴攤平 MongoDB Document，並收集所有葉子節點的鍵名。
     * * @param doc 要攤平的 MongoDB Document。
     * @param prefix 當前鍵的前綴 (例如 "user" 或 "address.city")。
     * @param flattenedMap 攤平後的結果 Map (鍵為攤平後的名稱，例如 "user.firstName")。
     * @param columnNamesSet 用於收集所有攤平後的欄位名稱 Set。
     */
    private void flattenDocument(Document doc, String prefix, Map<String, Object> flattenedMap, Set<String> columnNamesSet) {
        for (String key : doc.keySet()) {
            Object value = doc.get(key);
            String newKey = prefix.isEmpty() ? key : prefix + "." + key;

            if (value instanceof Document) {
                // 如果是巢狀文件，則遞迴處理
                flattenDocument((Document) value, newKey, flattenedMap, columnNamesSet);
            } else if (value instanceof List) {
                // 如果是陣列 (List)，我們將其視為一個完整的 JSON 結構。
                // 為了避免複雜的陣列元素攤平（例如 array[0].field），我們通常將陣列本身保留為一個物件。
                // 如果需要攤平陣列，邏輯會更複雜，此處選擇保留陣列。
                flattenedMap.put(newKey, value);
                columnNamesSet.add(newKey);
            } else {
                // 基本類型（葉子節點）
                flattenedMap.put(newKey, value);
                columnNamesSet.add(newKey);
            }
        }
    }

    /**
     * 執行 MongoDB 查詢操作，並將巢狀結構攤平。
     */
    public MongoQueryResult query(MongoQueryRequest requestBody) {
        MongoQueryResult result = new MongoQueryResult();

        try {
            MongoDatabase database = mongoClient.getDatabase(requestBody.getDatabaseName());
            MongoCollection<Document> collection = database.getCollection(requestBody.getCollection());

            // 1. 解析篩選條件 (Filter)
            Document filterDoc = new Document();
            if (requestBody.getFilter() != null && !requestBody.getFilter().isEmpty()) {
                filterDoc = Document.parse(requestBody.getFilter());
            }

            // 2. 獲取總筆數 (必須在應用 Skip/Limit 之前)
            long totalCount = collection.countDocuments(filterDoc);
            result.setTotalCount(totalCount);

            if (totalCount == 0) {
                result.setRows(Collections.emptyList());
                result.setColumnNames(Collections.emptyList());
                return result;
            }

            // 3. 執行 Find 操作
            FindIterable<Document> findIterable = collection.find(filterDoc);

            // 4. 應用排序 (Sort)
            if (requestBody.getSort() != null && !requestBody.getSort().isEmpty()) {
                Document sortDoc = Document.parse(requestBody.getSort());
                findIterable.sort(sortDoc);
            }

            // 5. 應用投影 (Projection)
            if (requestBody.getProjection() != null && !requestBody.getProjection().isEmpty()) {
                Document projectionDoc = Document.parse(requestBody.getProjection());
                findIterable.projection(projectionDoc);
            }

            // 6. 應用分頁 (Skip and Limit)
            int skip = (requestBody.getPage() - 1) * requestBody.getSize();
            findIterable.skip(skip).limit(requestBody.getSize());

            // 7. 處理結果集：攤平 Document 並收集所有欄位名稱
            List<Map<String, Object>> rows = new ArrayList<>();
            // 使用 LinkedHashSet 保持欄位順序
            Set<String> columnNamesSet = new LinkedHashSet<>();

            for (Document doc : findIterable) {
                Map<String, Object> flattenedRow = new LinkedHashMap<>();
                // 執行遞迴攤平
                flattenDocument(doc, "", flattenedRow, columnNamesSet);
                rows.add(flattenedRow);
            }

            result.setRows(rows);
            result.setColumnNames(new ArrayList<>(columnNamesSet));

        } catch (Exception e) {
            log.error("執行 MongoDB 查詢失敗: database={}, collection={}, filter={}, error={}",
                    requestBody.getDatabaseName(), requestBody.getCollection(),
                    requestBody.getFilter(), e.getMessage(), e);
            throw new RuntimeException("MongoDB 查詢失敗: " + e.getMessage());
        }

        return result;
    }

}
