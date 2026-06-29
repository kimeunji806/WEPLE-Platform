package com.weple.cloud.repository.web;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.weple.cloud.auth.service.LoginUserDetails;
import com.weple.cloud.project.service.ProjectService;
import com.weple.cloud.repository.service.GithubCommitDetailInfo;
import com.weple.cloud.repository.service.GithubCommitInfo;
import com.weple.cloud.repository.service.GithubRepositoryInfo;
import com.weple.cloud.repository.service.GithubRepositoryReader;
import com.weple.cloud.repository.service.GithubFileDiffInfo;
import com.weple.cloud.repository.service.RepositoryService;
import com.weple.cloud.repository.service.RepositoryVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/repository")
public class RepositoryController {

    private static final int DEFAULT_COMMIT_RANGE_DAYS = 30;
    private static final ZoneId DISPLAY_TIME_ZONE = ZoneId.of("Asia/Seoul");
    private static final DateTimeFormatter COMMIT_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(DISPLAY_TIME_ZONE);

    private final RepositoryService repositoryService;
    private final GithubRepositoryReader githubRepositoryReader;
    private final ProjectService projectService;

    // 설정 탭에서는 프로젝트에 등록된 저장소를 관리 목록으로 보여줌
    @GetMapping("/management")
    public String management(@AuthenticationPrincipal LoginUserDetails loginUser,
                             @RequestParam(defaultValue = "1") Long projectId,
                             Model model) {
        setRepositorySettingMenu(model, projectId);
        model.addAttribute("repositoryList",
                repositoryService.findRepositories(loginUser.getLoginUser().getCompanyId(), projectId));
        return "weple/repository/management";
    }

    // 저장소 추가 버튼에서만 진입하는 신규 등록 화면
    @GetMapping("/management/new")
    public String newRepository(@AuthenticationPrincipal LoginUserDetails loginUser,
                                @RequestParam(defaultValue = "1") Long projectId,
                                Model model) {
        setRepositorySettingMenu(model, projectId);
        RepositoryVO repository = model.containsAttribute("repository")
                ? (RepositoryVO) model.asMap().get("repository")
                : new RepositoryVO();
        repository.setProjectId(projectId);
        model.addAttribute("repository", repository);
        addMainRepositoryInfo(model, loginUser.getLoginUser().getCompanyId(), projectId, repository.getRepositoryId());
        return "weple/repository/register";
    }

    // 수정 화면에는 기존 값을 채우고, 다른 회사나 프로젝트의 저장소는 조회되지 않게 함
    @GetMapping("/management/edit")
    public String editRepository(@AuthenticationPrincipal LoginUserDetails loginUser,
                                 @RequestParam(defaultValue = "1") Long projectId,
                                 @RequestParam String repositoryId,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        setRepositorySettingMenu(model, projectId);
        RepositoryVO repository = repositoryService.findRepository(
                loginUser.getLoginUser().getCompanyId(), projectId, repositoryId);
        if (repository == null) {
            redirectAttributes.addFlashAttribute("repositoryError", "수정할 저장소를 찾을 수 없습니다.");
            return "redirect:/repository/management?projectId=" + projectId;
        }
        model.addAttribute("repository", repository);
        addMainRepositoryInfo(model, loginUser.getLoginUser().getCompanyId(), projectId, repository.getRepositoryId());
        return "weple/repository/register";
    }

    // 로그인된 회사 ID를 사용해 신규 저장소를 등록
    @PostMapping
    public String registerRepository(@AuthenticationPrincipal LoginUserDetails loginUser,
                                     RepositoryVO repository,
                                     RedirectAttributes redirectAttributes) {
        try {
            repository.setCompanyId(loginUser.getLoginUser().getCompanyId());
            repositoryService.registerRepository(repository);
            redirectAttributes.addFlashAttribute("repositorySuccess", "저장소가 등록되었습니다.");
            return "redirect:/repository/management?projectId=" + repository.getProjectId();
        } catch (IllegalArgumentException | IllegalStateException ex) {
            redirectAttributes.addFlashAttribute("repositoryError", ex.getMessage());
            redirectAttributes.addFlashAttribute("repository", repository);
            return "redirect:/repository/management/new?projectId=" + repository.getProjectId();
        } catch (DataIntegrityViolationException ex) {
            redirectAttributes.addFlashAttribute("repositoryError", "저장소 정보가 중복되었거나 연결 설정이 올바르지 않습니다.");
            redirectAttributes.addFlashAttribute("repository", repository);
            return "redirect:/repository/management/new?projectId=" + repository.getProjectId();
        }
    }

    // 수정 요청에도 로그인 회사 ID를 다시 설정해 요청값 조작을 막음
    @PostMapping("/update")
    public String updateRepository(@AuthenticationPrincipal LoginUserDetails loginUser,
                                   RepositoryVO repository,
                                   RedirectAttributes redirectAttributes) {
        try {
            repository.setCompanyId(loginUser.getLoginUser().getCompanyId());
            repositoryService.updateRepository(repository);
            redirectAttributes.addFlashAttribute("repositorySuccess", "저장소 정보가 수정되었습니다.");
            return "redirect:/repository/management?projectId=" + repository.getProjectId();
        } catch (IllegalArgumentException | IllegalStateException ex) {
            redirectAttributes.addFlashAttribute("repositoryError", ex.getMessage());
            redirectAttributes.addFlashAttribute("repository", repository);
            return "redirect:/repository/management/edit?projectId=" + repository.getProjectId()
                    + "&repositoryId=" + repository.getRepositoryId();
        }
    }

    // 삭제 확인용 입력값은 서비스에서 실제 저장소명과 비교한 후 삭제
    @PostMapping("/delete")
    public String deleteRepository(@AuthenticationPrincipal LoginUserDetails loginUser,
                                   @RequestParam(defaultValue = "1") Long projectId,
                                   @RequestParam String repositoryId,
                                   @RequestParam String confirmationName,
                                   RedirectAttributes redirectAttributes) {
        try {
            repositoryService.deleteRepository(loginUser.getLoginUser().getCompanyId(), projectId,
                    repositoryId, confirmationName);
            redirectAttributes.addFlashAttribute("repositorySuccess", "저장소가 삭제되었습니다.");
        } catch (IllegalArgumentException | IllegalStateException ex) {
            redirectAttributes.addFlashAttribute("repositoryError", ex.getMessage());
        }
        return "redirect:/repository/management?projectId=" + projectId;
    }

    // 프로젝트 사용자는 등록된 저장소 목록을 조회할 수 있음
    @GetMapping("/list")
    public String list(@AuthenticationPrincipal LoginUserDetails loginUser,
                       @RequestParam(defaultValue = "1") Long projectId,
                       Model model) {
        setProjectMenu(model, projectId);
        model.addAttribute("repositoryList",
                repositoryService.findRepositories(loginUser.getLoginUser().getCompanyId(), projectId));
        return "weple/repository/list";
    }

    // GitHub API 실패 시에도 상세 화면의 기본 구조가 유지되도록 빈 데이터를 준비함
    @GetMapping("/detail")
    public String detail(@AuthenticationPrincipal LoginUserDetails loginUser,
                         @RequestParam(defaultValue = "1") Long projectId,
                         @RequestParam(required = false) String repositoryId,
                         @RequestParam(required = false) String branch,
                         @RequestParam(required = false) String directoryPath,
                         @RequestParam(required = false) String filePath,
                         @RequestParam(defaultValue = "1") int commitPage,
                         @RequestParam(required = false) String commitStartDate,
                         @RequestParam(required = false) String commitEndDate,
                         @RequestParam(required = false) String commitSearch,
                         Model model) {
        setProjectMenu(model, projectId);
        LocalDate defaultEndDate = LocalDate.now(DISPLAY_TIME_ZONE);
        LocalDate selectedStartDate = parseCommitDate(commitStartDate, defaultEndDate.minusDays(DEFAULT_COMMIT_RANGE_DAYS));
        LocalDate selectedEndDate = parseCommitDate(commitEndDate, defaultEndDate);
        if (selectedStartDate.isAfter(selectedEndDate)) {
            LocalDate originalStartDate = selectedStartDate;
            selectedStartDate = selectedEndDate;
            selectedEndDate = originalStartDate;
        }
        List<RepositoryVO> repositoryList =
                repositoryService.findRepositories(loginUser.getLoginUser().getCompanyId(), projectId);
        RepositoryVO repository = repositoryId == null && !repositoryList.isEmpty()
                ? repositoryList.get(0)
                : repositoryService.findRepository(loginUser.getLoginUser().getCompanyId(), projectId, repositoryId);
        model.addAttribute("repository", repository);
        if (repository != null) {
            GithubRepositoryInfo githubInfo = new GithubRepositoryInfo();
            githubInfo.setDefaultBranch("main");
            githubInfo.setSelectedBranch("main");
            githubInfo.setBranches(List.of("main"));
            githubInfo.setFiles(List.of());
            githubInfo.setSelectedFileContent("GitHub 파일 정보를 불러오는 중입니다.");
            githubInfo.setCommits(List.of());
            githubInfo.setCommitPage(Math.max(commitPage, 1));
            githubInfo.setTotalCommitPages(1);
            githubInfo.setStartCommitPage(1);
            githubInfo.setEndCommitPage(1);
            try {
                githubInfo = githubRepositoryReader.readRepository(
                        repository.getRepositoryUrl(), branch, directoryPath, filePath, commitPage,
                        selectedStartDate, selectedEndDate);
                markExistingTaskLinks(projectId, githubInfo.getCommits());
            } catch (IllegalStateException ex) {
                model.addAttribute("githubError", ex.getMessage());
            }
            model.addAttribute("githubInfo", githubInfo);
        }
        model.addAttribute("commitStartDate", selectedStartDate);
        model.addAttribute("commitEndDate", selectedEndDate);
        model.addAttribute("commitSearch", commitSearch == null ? "" : commitSearch);
        return "weple/repository/detail";
    }

    // 커밋 메시지에서 인식한 TSK 코드가 현재 프로젝트에 존재하면 일감 상세 링크를 활성화합니다.
    private void markExistingTaskLinks(Long projectId, List<GithubCommitInfo> commits) {
        if (commits == null || commits.isEmpty()) {
            return;
        }
        List<String> taskIds = commits.stream()
                .map(GithubCommitInfo::getTaskId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        Map<String, String> taskTitleMap = repositoryService.findTaskTitles(projectId, taskIds);
        commits.forEach(commit -> {
            String taskId = commit.getTaskId();
            String taskTitle = taskId == null ? null : taskTitleMap.get(taskId);
            commit.setTaskTitle(taskTitle);
            commit.setTaskExists(taskTitle != null);
        });
    }

    // 커밋 기간 값이 비어 있거나 잘못 들어오면 기본 조회 기간으로 되돌립니다.
    private LocalDate parseCommitDate(String date, LocalDate defaultDate) {
        if (date == null || date.isBlank()) {
            return defaultDate;
        }
        try {
            return LocalDate.parse(date);
        } catch (DateTimeParseException ex) {
            return defaultDate;
        }
    }

    // 선택 파일의 최근 두 커밋을 GitHub에서 조회해 변경 비교 화면에 전달
    @GetMapping("/diff")
    public String diff(@AuthenticationPrincipal LoginUserDetails loginUser,
                       @RequestParam(defaultValue = "1") Long projectId,
                       @RequestParam String repositoryId,
                       @RequestParam String branch,
                       @RequestParam String filePath,
                       Model model) {
        setProjectMenu(model, projectId);
        RepositoryVO repository = repositoryService.findRepository(
                loginUser.getLoginUser().getCompanyId(), projectId, repositoryId);
        if (repository == null) {
            model.addAttribute("githubError", "비교할 저장소를 찾을 수 없습니다.");
            return "weple/repository/form";
        }

        model.addAttribute("repository", repository);
        model.addAttribute("branch", branch);
        try {
            GithubFileDiffInfo fileDiff = githubRepositoryReader.readFileDiff(
                    repository.getRepositoryUrl(), branch, filePath);
            model.addAttribute("fileDiff", fileDiff);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            model.addAttribute("githubError", ex.getMessage());
        }
        return "weple/repository/form";
    }

    // 커밋 목록에서 선택한 커밋을 WEPLE 내부 상세 화면으로 먼저 연결
    @GetMapping("/commit")
    public String commit(@AuthenticationPrincipal LoginUserDetails loginUser,
                         @RequestParam(defaultValue = "1") Long projectId,
                         @RequestParam String repositoryId,
                         @RequestParam(required = false) String branch,
                         @RequestParam String sha,
                         @RequestParam(required = false) String message,
                         @RequestParam(required = false) String authorEmail,
                         @RequestParam(required = false) String committedAt,
                         @RequestParam(required = false) String commitUrl,
                         Model model) {
        setProjectMenu(model, projectId);
        RepositoryVO repository = repositoryService.findRepository(
                loginUser.getLoginUser().getCompanyId(), projectId, repositoryId);
        GithubCommitDetailInfo commitDetail = createFallbackCommitDetail(sha, message, authorEmail, committedAt, commitUrl);
        if (repository != null) {
            try {
                commitDetail = githubRepositoryReader.readCommitDetail(repository.getRepositoryUrl(), sha);
            } catch (IllegalArgumentException | IllegalStateException ex) {
                model.addAttribute("githubError", ex.getMessage());
            }
        }
        model.addAttribute("repository", repository);
        model.addAttribute("branch", branch);
        model.addAttribute("displayBranch", branch == null || branch.isBlank() ? "main" : branch);
        model.addAttribute("commitDetail", commitDetail);
        return "weple/repository/commit";
    }

    // 커밋 상세 URL에 기존 ISO 날짜가 넘어와도 화면용 날짜로 다시 정리
    private GithubCommitDetailInfo createFallbackCommitDetail(String sha, String message, String authorEmail,
                                                              String committedAt, String commitUrl) {
        GithubCommitDetailInfo commitDetail = new GithubCommitDetailInfo();
        String safeSha = sha == null ? "" : sha;
        commitDetail.setSha(safeSha);
        commitDetail.setShortSha(safeSha.substring(0, Math.min(safeSha.length(), 8)));
        commitDetail.setMessage(message);
        commitDetail.setAuthorEmail(authorEmail);
        commitDetail.setCommittedAt(formatCommitDate(committedAt));
        commitDetail.setCommitUrl(commitUrl);
        commitDetail.setFiles(List.of());
        commitDetail.setFileTree(List.of());
        return commitDetail;
    }

    private String formatCommitDate(String committedAt) {
        if (committedAt == null || committedAt.isBlank() || "-".equals(committedAt)) {
            return "-";
        }

        try {
            return COMMIT_DATE_FORMATTER.format(Instant.parse(committedAt));
        } catch (DateTimeParseException ex) {
            return committedAt;
        }
    }

    // 프로젝트 공통 레이아웃에서 저장소 메뉴를 활성화
    private void setProjectMenu(Model model, Long projectId) {
        setProjectHeader(model, projectId);
        model.addAttribute("projectId", projectId);
        model.addAttribute("sidebarMenu", "project");
        model.addAttribute("currentMenu", "repository");
    }

    // 설정 안의 저장소 화면에서는 프로젝트 헤더의 설정 탭을 활성화
    private void setRepositorySettingMenu(Model model, Long projectId) {
        setProjectHeader(model, projectId);
        model.addAttribute("projectId", projectId);
        model.addAttribute("sidebarMenu", "project");
        model.addAttribute("currentMenu", "setting");
        model.addAttribute("settingMenu", "repository");
    }

    // 프로젝트 공통 헤더가 프로젝트명을 표시할 수 있도록 현재 프로젝트 정보를 모델에 담음
    private void setProjectHeader(Model model, Long projectId) {
        model.addAttribute("project", projectService.findById(String.valueOf(projectId)));
    }

    // 등록 또는 수정 대상과 다른 주 저장소가 있을 때만 변경 확인 창을 표시
    private void addMainRepositoryInfo(Model model, Long companyId, Long projectId, String repositoryId) {
        RepositoryVO currentMainRepository = repositoryService.findMainRepository(companyId, projectId);
        boolean mainChangeRequired = currentMainRepository != null
                && !currentMainRepository.getRepositoryId().equals(repositoryId);
        model.addAttribute("currentMainRepository", currentMainRepository);
        model.addAttribute("mainChangeRequired", mainChangeRequired);
    }

}
