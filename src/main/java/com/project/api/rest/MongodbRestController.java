package com.project.api.rest;

import com.project.model.request.CollectionRequest;
import com.project.model.request.MongoQueryRequest;
import com.project.model.response.ApiResponse;
import com.project.model.response.ApiResponseMapper;
import com.project.model.response.MongoQueryResult;
import com.project.service.MongoDBService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * MongodbRestController 用於處理 MongoDB 相關的 API 請求。
 */
@RestController
@RequestMapping("/api/mongodb")
@RequiredArgsConstructor
public class MongodbRestController {

    private final MongoDBService mongoDBService;
    private final ApiResponseMapper apiResponseMapper;

    /**
     * 獲取 MongoDB 資料庫中的所有 DB。
     */
    @PostMapping("/databases")
    public ApiResponse<List<String>> getDatabase() {
        List<String> schemas = mongoDBService.getDatabases();
        return apiResponseMapper.success("0", schemas);
    }

    /**
     * 獲取 Database 中的所有 Collections。
     */
    @PostMapping("/collections")
    public ApiResponse<List<String>> getCollections(
            @RequestBody CollectionRequest requestBody) {
        List<String> collections = mongoDBService.getCollections(requestBody);
        return apiResponseMapper.success("0", collections);
    }

    /**
     * 執行 MongoDB 查詢操作。
     */
    @PostMapping("/query")
    public ApiResponse<MongoQueryResult> query(
            @RequestBody MongoQueryRequest requestBody) {
        MongoQueryResult mongoQueryResult = mongoDBService.query(requestBody);
        return apiResponseMapper.success("0", mongoQueryResult);
    }


}
