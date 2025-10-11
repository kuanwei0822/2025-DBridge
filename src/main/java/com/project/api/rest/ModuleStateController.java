package com.project.api.rest;

import com.project.model.response.ApiResponse;
import com.project.model.response.ApiResponseMapper;
import com.project.model.response.LoginModuleResponse;
import com.project.service.ModuleStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用於取得 模組資訊 相關的 API 請求。
 * 注意 : 此 Controller 為無狀態的 API, 不應該能更改任何資料，只能查詢狀態。
 * 不用登入驗證即可查詢。
 * 1. 登入模組 : 登入、非登入
 */
@RestController
@RequestMapping("/api/module")
@RequiredArgsConstructor
public class ModuleStateController {

    private final ModuleStateService moduleStateService;
    private final ApiResponseMapper apiResponseMapper;

    /**
     * 查詢是否啟用登入驗證
     * 這支 API 不需要登入驗證
     */
    @PostMapping("/status/auth")
    public ApiResponse<LoginModuleResponse> getModuleOfLogin() {
        LoginModuleResponse result = moduleStateService.getModuleOfLogin();
        return apiResponseMapper.success("0", result);
    }
}
