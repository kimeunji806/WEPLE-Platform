package com.weple.cloud.web;

import java.util.Collections;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.weple.cloud.auth.service.LoginUserDetails;
import com.weple.cloud.auth.service.LoginUserVO;
import com.weple.cloud.project.service.ProjectService;
import com.weple.cloud.repository.service.RepositoryService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@ControllerAdvice(annotations = Controller.class)
@RequiredArgsConstructor
public class SecurityModelAdvice {

    private final RepositoryService repositoryService;
    private final ProjectService projectService;

    // 프로젝트 설정 권한 코드 (ProjectController.PERM_SELECT와 동일한 값)
    private static final String PERM_SELECT = "k1_select";

    // 상단 메뉴에서 가입승인 탭을 노출할 수 있는지 판단합니다.
    @ModelAttribute("canApproveSignup")
    public boolean canApproveSignup() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }

        return authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_COMPANY_OWNER".equals(authority.getAuthority())
                        || "ROLE_COMPANY_ADMIN".equals(authority.getAuthority()));
    }
    
    // 공통 헤더에 프로필 아이콘/이름에서 사용할 로그인 사용자 정보-은지
    @ModelAttribute("currentUser")
    public LoginUserVO currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof LoginUserDetails)) {
            return null;
        }
        return ((LoginUserDetails) authentication.getPrincipal()).getLoginUser();
    }

    // 프로젝트에 저장소가 하나 이상 등록된 경우에만 저장소 탭을 노출합니다.
    @ModelAttribute("hasRepository")
    public boolean hasRepository(HttpServletRequest request) {
        String projectId = request.getParameter("projectId");
        Long currentProjectId = 1L;
        if (projectId != null) {
            try {
                currentProjectId = Long.valueOf(projectId);
            } catch (NumberFormatException ignored) {
                // 프로젝트 번호가 잘못된 요청은 현재 기본 프로젝트 기준으로 처리합니다.
            }
        }
        return repositoryService.hasRepository(currentProjectId);
    }
    
    // 선택한 모듈만 노출 - 은지
    @ModelAttribute("moduleNames")
    public List<String> moduleNames(HttpServletRequest request) {
        String projectId = request.getParameter("projectId");
 
        // Path Variable도 처리 (/project/{id}/setting 등)
        if (projectId == null) {
            String uri = request.getRequestURI();
            java.util.regex.Matcher m =
                java.util.regex.Pattern.compile("/project/(\\d+)").matcher(uri);
            if (m.find()) projectId = m.group(1);
        }
 
        if (projectId == null) return Collections.emptyList();
        try {
            return projectService.findActiveModuleNames(Long.valueOf(projectId));
        } catch (NumberFormatException e) {
            return Collections.emptyList();
        }
    }
    
    @ModelAttribute("isCompanyManager")
    public boolean isCompanyManager() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof LoginUserDetails)) {
            return false;
        }
        LoginUserVO user = ((LoginUserDetails) authentication.getPrincipal()).getLoginUser();
        return Integer.valueOf(1).equals(user.getOwnerYn())
            || Integer.valueOf(1).equals(user.getAdminYn());
    }

    // 기업 최고관리자(owner)인지 여부. 관리자(admin)는 포함하지 않음 - "관리-설정" 같은
    // owner 전용 화면/탭을 노출할지 판단할 때 사용
    @ModelAttribute("isCompanyOwner")
    public boolean isCompanyOwner() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof LoginUserDetails)) {
            return false;
        }
        LoginUserVO user = ((LoginUserDetails) authentication.getPrincipal()).getLoginUser();
        return Integer.valueOf(1).equals(user.getOwnerYn());
    }

    // 현재 프로젝트에서 "설정" 탭(모듈 저장 권한)을 노출할지 여부.
    // owner/admin은 항상 true, 그 외 사용자는 역할로 부여받은 k1_select 권한이 있을 때만 true.
    // 프로젝트 서브메뉴(navbar.html)에서 어느 탭에 있든 일관되게 "설정" 탭이 보이도록 전역으로 판단한다.
    @ModelAttribute("canSaveSetting")
    public boolean canSaveSetting(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof LoginUserDetails)) {
            return false;
        }
        LoginUserVO user = ((LoginUserDetails) authentication.getPrincipal()).getLoginUser();

        if (Integer.valueOf(1).equals(user.getOwnerYn()) || Integer.valueOf(1).equals(user.getAdminYn())) {
            return true;
        }

        String projectId = request.getParameter("projectId");
        if (projectId == null) {
            String uri = request.getRequestURI();
            java.util.regex.Matcher m =
                java.util.regex.Pattern.compile("/project/(\\d+)").matcher(uri);
            if (m.find()) projectId = m.group(1);
        }
        if (projectId == null) return false;

        try {
            java.util.Set<String> perms = projectService.findProjectPermissionCodes(user.getUserCode(), Long.valueOf(projectId));
            return perms != null && perms.contains(PERM_SELECT);
        } catch (NumberFormatException e) {
            return false;
        }
    }
}