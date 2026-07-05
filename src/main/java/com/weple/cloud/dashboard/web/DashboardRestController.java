package com.weple.cloud.dashboard.web;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weple.cloud.auth.service.LoginUserDetails;
import com.weple.cloud.dashboard.service.DashboardService;
import com.weple.cloud.project.service.ProjectVO;
import com.weple.cloud.task.service.TaskVO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardRestController {

    private final DashboardService dashboardService;

    // D-7 마감 임박 일감 가져오기 API
    @GetMapping("/upcoming-tasks")
    public ResponseEntity<?> getUpcomingTasks(@AuthenticationPrincipal LoginUserDetails loginUser) {
        // 별도의 권한 검증 없이 바로 내 일감 조회 진행
        String userCode = loginUser.getLoginUser().getUserCode();
        List<TaskVO> tasks = dashboardService.getTasksDueWithinAWeek(userCode);
        
        return ResponseEntity.ok(tasks);
    }
    
 // 내가 참여 중인 프로젝트 목록 가져오기 API (권한 검증 제외)
    @GetMapping("/my-projects")
    public ResponseEntity<?> getMyProjects(@AuthenticationPrincipal LoginUserDetails loginUser) {
        String userCode = loginUser.getLoginUser().getUserCode();
        
        // 이전에 매퍼(selectAllByMember) 뼈대를 주셨던 로직을 서비스 레이어를 통해 호출합니다.
        // TIP: 이미지처럼 진척도(%)나 Task 개수를 표현하기 위해 ProjectVO에 관련 필드가 추가되어 있으면 좋습니다.
        List<ProjectVO> projects = dashboardService.getProjectsByMember(userCode);
        
        return ResponseEntity.ok(projects);
    }
}

