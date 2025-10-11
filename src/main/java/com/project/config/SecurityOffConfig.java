package com.project.config;

import com.project.config.filter.LoginBypassFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security 相關設定
 * - 當 'login.active' 屬性設置為 false 時啟用
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@ConditionalOnProperty(name = "login.active", havingValue = "false")
public class SecurityOffConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }
}

//
///**
// * Security 相關設定
// * - 當 'login.active' 屬性設置為 false 時啟用
// */
//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//@ConditionalOnProperty(name = "login.active", havingValue = "false")
//public class SecurityOffConfig {
//
//    @Bean
//    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
////                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
//                .addFilterBefore(loginBypassFilter(), UsernamePasswordAuthenticationFilter.class); // 訪客模式下，Login 自動繞過登入頁面，轉導至 hoeme 頁面
//        return http.build();
//    }
//
//    @Bean
//    public LoginBypassFilter loginBypassFilter() {
//        LoginBypassFilter filter = new LoginBypassFilter();
//        return filter;
//    }
//}
