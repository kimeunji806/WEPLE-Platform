package com.weple.cloud.project.web;

import java.util.List;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.weple.cloud.auth.service.LoginUserDetails;
import com.weple.cloud.notification.service.AlarmType;
import com.weple.cloud.notification.service.NotificationService;
import com.weple.cloud.project.service.ProjectMemberRoleVO;
import com.weple.cloud.project.service.ProjectMemberService;
import com.weple.cloud.project.service.ProjectMemberVO;
import com.weple.cloud.project.service.ProjectService;
import com.weple.cloud.project.service.ProjectVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ProjectMemberController {

    private final ProjectMemberService memberService;
    private final ProjectService projectService;
    private final NotificationService notificationService;
    private static final String PERM_MEMBER = "k1_member";
    
    private Set<String> findMemberPermissions(LoginUserDetails loginUser, Long projectId) {
        com.weple.cloud.auth.service.LoginUserVO user = loginUser.getLoginUser();
        if (isCompanyManager(user)) {
            return Set.of(PERM_MEMBER);
        }
        return memberService.findProjectPermissionCodes(user.getUserCode(), projectId);
    }

    private boolean hasPerm(Set<String> perms, String code) {
        return perms != null && perms.contains(code);
    }

    private boolean isCompanyManager(com.weple.cloud.auth.service.LoginUserVO user) {
        return Integer.valueOf(1).equals(user.getOwnerYn())
            || Integer.valueOf(1).equals(user.getAdminYn());
    }

    //  설정 > 구성원 탭
    @GetMapping("/project/settings/members")
    public String membersPage(
    		@AuthenticationPrincipal LoginUserDetails loginUser,
            @RequestParam Long projectId,
            Model model) {
    	
    	try {
            if (!isCompanyManager(loginUser.getLoginUser())
                    && !memberService.isMember(loginUser.getLoginUser().getUserCode(), projectId)) {
                return "weple/access-denide";
            }
        } catch (Exception e) {
            return "weple/access-denide";
        }
    	
    	Set<String> perms = findMemberPermissions(loginUser, projectId);
        // 멤버도 아니고 관리자도 아니면 차단
        if (!isCompanyManager(loginUser.getLoginUser())
                && !memberService.isMember(loginUser.getLoginUser().getUserCode(), projectId)) {
            return "weple/access-denide";
        }

        Long companyId = loginUser.getLoginUser().getCompanyId();

        List<ProjectMemberVO> memberList = memberService.findMemberList(projectId);
        List<ProjectMemberRoleVO> roleList   = memberService.findRoleList();
        List<ProjectMemberVO> groupList  = memberService.findGroupList(companyId);

        model.addAttribute("project", projectService.findById(String.valueOf(projectId)));
        model.addAttribute("moduleNames", projectService.findActiveModuleNames(projectId));
        model.addAttribute("sidebarMenu", "project");
        model.addAttribute("currentMenu", "setting");
        model.addAttribute("projectId", projectId);
        model.addAttribute("memberList", memberList);
        model.addAttribute("roleList", roleList);
        model.addAttribute("groupList", groupList);
        model.addAttribute("activeTab", "member");
        model.addAttribute("settingMenu", "member");
        model.addAttribute("canManageMember", hasPerm(perms, PERM_MEMBER));

        return "weple/project/members";
    }

    //  구성원 추가 모달 - 사용자 검색 AJAX (같은 회사 사용자만, 이미 소속된 사람도 포함해서 표시)
    @GetMapping("/project/settings/members/search")
    @ResponseBody
    public ResponseEntity<List<ProjectMemberVO>> searchUsers(
    		@AuthenticationPrincipal LoginUserDetails loginUser,
            @RequestParam Long projectId,
            @RequestParam(required = false, defaultValue = "") String keyword) {

        Set<String> perms = findMemberPermissions(loginUser, projectId);
        if (!hasPerm(perms, PERM_MEMBER)) {
            return ResponseEntity.status(403).build();
        }

        Long companyId = loginUser.getLoginUser().getCompanyId();
        List<ProjectMemberVO> result = memberService.searchUsersForAdd(projectId, keyword, companyId);
        return ResponseEntity.ok(result);
    }

    //  그룹별 사용자 조회
    @GetMapping("/project/settings/members/group")
    @ResponseBody
    public ResponseEntity<List<ProjectMemberVO>> getUsersByGroup(
    		@AuthenticationPrincipal LoginUserDetails loginUser,
            @RequestParam Long groupId,
            @RequestParam Long projectId) {

        Set<String> perms = findMemberPermissions(loginUser, projectId);
        if (!hasPerm(perms, PERM_MEMBER)) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(memberService.findUsersByGroupId(groupId, projectId));
    }

    //  구성원 추가
    @PostMapping("/project/settings/members/add")
    @ResponseBody
    public ResponseEntity<String> addMember(
    		@AuthenticationPrincipal LoginUserDetails loginUser,
            @RequestParam Long projectId,
            @RequestParam String userCode,
            @RequestParam Long roleId) {
    	
    	 Set<String> perms = findMemberPermissions(loginUser, projectId);
         if (!hasPerm(perms, PERM_MEMBER)) {
             return ResponseEntity.status(403).body("구성원 관리 권한이 없습니다.");
         }

        ProjectMemberVO vo = new ProjectMemberVO();
        vo.setProjectId(projectId);
        vo.setUserCode(userCode);
        vo.setRoleId(roleId);

        try {
            memberService.addMember(vo);
            // 알림-은지(프로젝트 초대)
            ProjectVO project = projectService.findById(String.valueOf(projectId));
            String projectTitle = (project != null) ? project.getProjectTitle() : "프로젝트";
            
            notificationService.create(
                    userCode,
                    AlarmType.TAG_PROJECT_INVITE,
                    "\"" + projectTitle + "\" 프로젝트에 참여자로 등록되었습니다.",
                    AlarmType.TARGET_PROJECT,
                    String.valueOf(projectId)
                );
            
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    //  구성원 삭제
    @PostMapping("/project/settings/members/delete")
    @ResponseBody
    public ResponseEntity<String> deleteMember(
    		@AuthenticationPrincipal LoginUserDetails loginUser,
            @RequestParam Long memberId,
            @RequestParam Long projectId) {
    	
    	Set<String> perms = findMemberPermissions(loginUser, projectId);
        if (!hasPerm(perms, PERM_MEMBER)) {
            return ResponseEntity.status(403).body("구성원 관리 권한이 없습니다.");
        }

        try {
            memberService.removeMember(memberId, projectId);
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}