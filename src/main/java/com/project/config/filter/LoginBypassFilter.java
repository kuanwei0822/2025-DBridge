package com.project.config.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

/**
 * LoginBypassFilter 用於在訪客模式下自動繞過登入頁面
 * 當應用程式設定為訪客模式 (login.active = false) 時，
 * 該過濾器會攔截對 /login 頁面的請求，並自動導向到 /home 頁面
 * [重要] 不要設為 @Component，否則會全域套用所有 API
 */
public class LoginBypassFilter implements Filter {

    // 💡 注入應用程式設定檔中的 login.active 屬性值
    @Value("${login.active}")
    private boolean loginActive;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest fRequest = (HttpServletRequest) request;
        HttpServletResponse fResponse = (HttpServletResponse) response;

        // 取得當前請求的路徑 (只檢查 /login 頁面)
        String path = fRequest.getServletPath();

        // 檢查條件：
        // 1. loginActive 為 false (訪客模式)
        // 2. 請求路徑剛好是 /login
        if (!loginActive && path.endsWith("/login")) {
            fResponse.sendRedirect("/home"); // 執行導向到 /home
            return;
        }

        chain.doFilter(request, fResponse);
    }
}