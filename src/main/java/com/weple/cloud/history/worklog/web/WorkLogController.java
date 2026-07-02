package com.weple.cloud.history.worklog.web;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.weple.cloud.admin.service.UserService;
import com.weple.cloud.admin.service.UserVO;
import com.weple.cloud.auth.service.LoginUserDetails;
import com.weple.cloud.history.worklog.service.WorkLogService;
import com.weple.cloud.history.worklog.service.WorkLogVO;
import com.weple.cloud.project.service.ProjectService;
import com.weple.cloud.project.service.ProjectVO;

@Controller
public class WorkLogController {
    private final WorkLogService workLogService;
    private final ProjectService projectService;
    private final UserService userService;

    @Autowired
    public WorkLogController(
            WorkLogService workLogService,
            ProjectService projectService,
            UserService userService) {
        this.workLogService = workLogService;
        this.projectService = projectService;
        this.userService = userService;
    }

    private boolean isCompanyManager(com.weple.cloud.auth.service.LoginUserVO user) {
        return Integer.valueOf(1).equals(user.getOwnerYn())
            || Integer.valueOf(1).equals(user.getAdminYn());
    }

    // 작업내역 조회
    @GetMapping("/worklog")
    public String workLogList(
            @AuthenticationPrincipal LoginUserDetails loginUser,
            @RequestParam(required = false) String projectId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String userCode,
            @RequestParam(required = false) List<String> typeNames,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "search", required = false) String search,
            Model model) {

        // 날짜 기본값: 최근 5일
        if (startDate == null || startDate.isEmpty() || endDate == null || endDate.isEmpty()) {
            LocalDate today = LocalDate.now();
            StringBuilder redirectUrl = new StringBuilder("redirect:/worklog");
            redirectUrl.append("?startDate=").append(today.minusDays(4).format(DateTimeFormatter.ofPattern("yyyy.MM.dd")));
            redirectUrl.append("&endDate=").append(today.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")));
            if (projectId != null && !projectId.isEmpty())
                redirectUrl.append("&projectId=").append(projectId);
            if (userCode != null && !userCode.isEmpty())
                redirectUrl.append("&userCode=").append(userCode);
            if (typeNames != null)
                for (String t : typeNames) redirectUrl.append("&typeNames=").append(t);
            return redirectUrl.toString();
        }

        List<WorkLogVO> list = null;
        int totalPages = 0;
        String targetDate = null;
        Double totalSpentHour = null;

        // 기본 기간(최근 5일)으로 들어와도 바로 조회되어야 하므로 search 파라미터로 게이트하지 않음.
        // "검색" 버튼은 필터(프로젝트/담당자/유형) 값을 조회 조건에 반영하는 용도로만 사용한다.
        List<String> allDates = workLogService.findDistinctDates(
                projectId, startDate, endDate, userCode, typeNames);

        totalPages = allDates.size();

        if (!allDates.isEmpty() && page <= allDates.size()) {
            targetDate = allDates.get(page - 1);
            list = workLogService.findByDate(targetDate, projectId, userCode, typeNames);
        }

        totalSpentHour = workLogService.sumSpentHour(projectId, startDate, endDate, userCode, typeNames);

        // 관리자: 전체 프로젝트 + 전체 사용자 / 일반 사용자: 참여 프로젝트만 + 사용자 필터 없음
        List<ProjectVO> projectList;
        List<UserVO> userList;

        if (isCompanyManager(loginUser.getLoginUser())) {
            projectList = projectService.findAll("");
            userList    = userService.findAllActiveUsers();
        } else {
            String myUserCode = loginUser.getLoginUser().getUserCode();
            projectList = projectService.findAllByMember(myUserCode, "", 0, Integer.MAX_VALUE);
            userList    = List.of();
        }

        model.addAttribute("projects", projectList);
        model.addAttribute("users",    userList);

        model.addAttribute("workLogList",     list);
        model.addAttribute("targetDate",      targetDate);
        model.addAttribute("searched",        true);
        model.addAttribute("projectId",       projectId);
        model.addAttribute("startDate",       startDate);
        model.addAttribute("endDate",         endDate);
        model.addAttribute("userCode",        userCode);
        model.addAttribute("typeNames",       typeNames);
        model.addAttribute("currentPage",     page);
        model.addAttribute("totalPages",      totalPages);
        model.addAttribute("totalSpentHour",  totalSpentHour);

        model.addAttribute("sidebarMenu", "work-history");
        model.addAttribute("currentMenu", "none");

        return "weple/history/worklog";
    }
}