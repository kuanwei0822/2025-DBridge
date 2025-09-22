package com.project.model.response;

import lombok.Builder;
import lombok.Data;

/**
 * API 回應的標準格式。
 */
@Data
@Builder
public class ApiResponse<T> {

    // 狀態碼。0 代表成功，其他代表失敗
    private String code;
    // 訊息。描述成功或失敗的原因
    private String message;
    // 回傳的資料。可以是任何類型的物件
    private T data;

}
