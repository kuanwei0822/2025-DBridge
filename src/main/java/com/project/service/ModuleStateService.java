package com.project.service;

import com.project.model.response.LoginModuleResponse;
import com.project.module.ModuleManager;
import com.project.module.login.LoginModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 用於取得 模組資訊 相關的 API 請求。
 * 1. 登入模組 : 登入、非登入
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ModuleStateService {

    private final ModuleManager moduleManager; // 模組管理器

    public LoginModuleResponse getModuleOfLogin() {

        // 取得登入模組資訊
        LoginModule loginModule = moduleManager.getLoginModule();

        // 最終回覆物件
        LoginModuleResponse result = new LoginModuleResponse();

        // 是否啟用登入模組放入結果
        boolean active = loginModule.isActive();
        result.setActive(active);

        return result;
    }
}
