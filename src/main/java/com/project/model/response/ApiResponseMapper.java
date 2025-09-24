package com.project.model.response;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * ApiResponseMapper 回覆包裝類別。
 * 用於將處理結果轉換為統一的 REST API 回應格式。
 * 提供了多種方法來生成不同類型的回應，包括成功回應、失敗回應、其他自定義回應。
 */
@Slf4j
@Component
public class ApiResponseMapper {

    /**
     * 將處理結果轉換為成功的 REST API 回應格式。
     * 資料使用 List<?> dataList 來表示，這樣可以靈活地處理不同類型的資料。
     */
    public <T> ApiResponse<T> success(String code, T data) {
        return ApiResponse.<T>builder()
                .code(code)
                .message("success")
                .data(data)
                .build();
    }

}
