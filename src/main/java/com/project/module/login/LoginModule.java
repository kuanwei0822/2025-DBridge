package com.project.module.login;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * LoginModule 用於表示登入模組的狀態
 * - LOGIN_ACTIVE: 登入模組啟用
 * - LOGIN_INACTIVE: 登入模組停用 (訪客模式)
 */
@RequiredArgsConstructor
public enum LoginModule {

    // 登入模組啟用
    LOGIN_ACTIVE(true),
    // 登入模組停用 (訪客模式)
    LOGIN_INACTIVE(false);

    @Getter
    private final boolean isActive;



}
