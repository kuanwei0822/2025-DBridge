package com.project.module;

import com.project.module.login.LoginModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * ModuleManager (模組管理器) 負責管理應用程式的模組
 * 目前支援模組 :
 * - 登入模組 (Login Module)
 */
@Component
public class ModuleManager {

    // 是否啟用登入
    @Value("${login.active}")
    private boolean loginActive;

    /**
     * 取得登入模組
     */
    public LoginModule getLoginModule() {
        return loginActive ? LoginModule.LOGIN_ACTIVE : LoginModule.LOGIN_INACTIVE;
    }

}
