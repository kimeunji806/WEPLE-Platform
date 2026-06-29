package com.weple.cloud.auth.impl;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.weple.cloud.auth.mapper.LoginMapper;
import com.weple.cloud.auth.service.LoginUserVO;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CompanyLoginValidationFilter extends OncePerRequestFilter {

    private static final String COMPANY_LOGIN_ERROR_MESSAGE =
            "\uD68C\uC0AC \uB85C\uADF8\uC778 \uC8FC\uC18C\uC640 \uC0AC\uC6A9\uC790 \uD68C\uC0AC \uC815\uBCF4\uAC00 \uC77C\uCE58\uD558\uC9C0 \uC54A\uC2B5\uB2C8\uB2E4.";

    private final LoginMapper loginMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (!isLoginRequest(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String requestedCompanyCode = resolveRequestedCompanyCode(request);
        if (requestedCompanyCode == null || requestedCompanyCode.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        String loginId = request.getParameter("loginId");
        if (loginId == null || loginId.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        LoginUserVO loginUser = loginMapper.selectLoginUserByLoginId(loginId);

        // Block before password authentication when URL company and user company differ.
        if (loginUser != null && !requestedCompanyCode.equalsIgnoreCase(loginUser.getCompanyCode())) {
            String message = URLEncoder.encode(COMPANY_LOGIN_ERROR_MESSAGE, StandardCharsets.UTF_8);
            response.sendRedirect(request.getContextPath() + "/c/" + requestedCompanyCode + "/login?error=" + message);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isLoginRequest(HttpServletRequest request) {
        return "POST".equalsIgnoreCase(request.getMethod())
                && (request.getContextPath() + "/login").equals(request.getRequestURI());
    }

    private String resolveRequestedCompanyCode(HttpServletRequest request) {
        String companyCode = request.getParameter("companyCode");
        if (companyCode != null && !companyCode.isBlank()) {
            return companyCode;
        }

        Object sessionCompanyCode = request.getSession().getAttribute("LOGIN_COMPANY_CODE");
        return sessionCompanyCode instanceof String value ? value : null;
    }
}
