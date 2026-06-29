package com.weple.cloud.project.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.weple.cloud.project.service.ProjectService;
import com.weple.cloud.project.service.ProjectVO;

@Controller
public class ProjectController {

    private final ProjectService projectService;

    // 전체 모듈 목록
    // module_mapping 테이블의 module_name 값과 동일하게 맞춰야 함
    private static final List<Map<String, String>> ALL_MODULES;
    static {
        ALL_MODULES = new ArrayList<>();
        for (String[] m : new String[][]{
            {"b1",  "개요"},
            {"b2",  "작업내역"},
            {"b3",  "마일스톤"},
            {"b4",  "일감"},
            {"b5",  "소요시간"},
            {"b6",  "간트차트"},
            {"b12", "테스트"},
            {"b7",  "위키"},
            {"b13", "파일관리"},
            {"b8",  "저장소"},
            {"b9",  "칸반보드"},
            {"b10", "캘린더"},
            {"b11", "설정"}
        }) {
            Map<String, String> map = new HashMap<>();
            map.put("name",  m[0]);
            map.put("label", m[1]);
            ALL_MODULES.add(map);
        }
    }

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // 프로젝트 목록 조회(페이징)
    @GetMapping("/project")
    public String projectList(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page,
            Model model) {

        int pageSize = 10;
        int offset = (page - 1) * pageSize;

        List<ProjectVO> list = projectService.findAll(keyword, offset, pageSize);
        int totalCount = projectService.countAll(keyword);
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        model.addAttribute("projects", list);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        model.addAttribute("sidebarMenu", "project");
        model.addAttribute("currentMenu", "none");
        return "weple/project/list";
    }

    // 설정 페이지 - 프로젝트 탭 조회
    @GetMapping("/project/{projectId}/setting")
    public String settingPage(@PathVariable Long projectId, Model model) {

        // 관리에서 선택된 모듈 전체 목록 (체크박스 표시 기준, is_active 무관)
        List<String> adminModules = projectService.findModuleNames(projectId);
        Set<String> adminSet = new HashSet<>(adminModules);

        // 현재 is_active = 'Y'인 모듈 (체크 여부 기준)
        List<String> activeModules = projectService.findActiveModuleNames(projectId);
        Set<String> activeSet = new HashSet<>(activeModules);

        // 관리에서 선택된 모듈만 표시, b11(설정)은 숨김
        List<Map<String, Object>> modules = new ArrayList<>();
        for (Map<String, String> m : ALL_MODULES) {
            String name = m.get("name");

            // b11(설정)은 항상 강제 포함이므로 화면에서 숨김
            if ("b11".equals(name)) continue;

            // 관리에서 선택된 모듈만 체크박스로 표시
            if (!adminSet.contains(name)) continue;

            Map<String, Object> item = new HashMap<>();
            item.put("name",    name);
            item.put("label",   m.get("label"));
            // is_active = 'Y'인 것만 체크 표시
            item.put("checked", activeSet.contains(name));
            modules.add(item);
        }

        ProjectVO setting = projectService.findSettingById(projectId);

        model.addAttribute("setting", setting);
        model.addAttribute("modules", modules);
        model.addAttribute("moduleNames", activeModules);
        model.addAttribute("projectId", projectId);
        model.addAttribute("project", setting);
        model.addAttribute("sidebarMenu", "project");
        model.addAttribute("currentMenu", "setting");
        model.addAttribute("activeTab", "project");
        model.addAttribute("settingMenu", "project");
        return "weple/project/setting";
    }

    // 설정 페이지 - 프로젝트 탭 저장
    @PostMapping("/project/{projectId}/setting")
    public String saveSetting(@PathVariable Long projectId,
                              @ModelAttribute ProjectVO vo,
                              RedirectAttributes ra) {
        vo.setProjectId(projectId);

        // b11(설정)은 항상 강제 포함
        List<String> modules = vo.getModuleNames();
        if (modules == null) modules = new ArrayList<>();
        if (!modules.contains("b11")) modules.add("b11");
        vo.setModuleNames(modules);

        // saveProjectSetting 내부에서 delete+insert 대신 is_active 업데이트
        projectService.saveProjectSetting(vo);

        ra.addFlashAttribute("toastMessage", "설정이 저장되었습니다.");
        return "redirect:/project/" + projectId + "/setting";
    }
}