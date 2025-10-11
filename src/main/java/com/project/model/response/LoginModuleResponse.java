package com.project.model.response;

import lombok.Data;

/**
 * 查詢是否啟用登入驗證 回應參數。
 */
@Data
public class LoginModuleResponse {

    private boolean active;
}
