package com.weple.cloud.dashboard.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weple.cloud.auth.service.LoginUserDetails;
import com.weple.cloud.dashboard.service.DashboardService;
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
}
