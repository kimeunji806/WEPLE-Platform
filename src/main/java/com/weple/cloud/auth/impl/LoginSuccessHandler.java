package com.weple.cloud.auth.impl;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.weple.cloud.auth.mapper.LoginMapper;
import com.weple.cloud.auth.service.LoginUserDetails;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final LoginMapper loginMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        Object principal = authentication.getPrincipal();

        // 로그인 성공 시점에 USERS.LAST_LOGIN_TIME을 현재 시각으로 갱신합니다.
        if (principal instanceof LoginUserDetails loginUserDetails) {
            loginMapper.updateLastLoginTime(loginUserDetails.getLoginUser().getUserCode());
        }

        setDefaultTargetUrl("/");
        setAlwaysUseDefaultTargetUrl(true);
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
