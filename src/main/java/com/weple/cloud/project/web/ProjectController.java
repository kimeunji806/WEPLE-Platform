package com.weple.cloud.project.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.weple.cloud.project.service.ProjectService;
import com.weple.cloud.project.service.ProjectVO;

@Controller
public class ProjectController {
	private final ProjectService projectService;
	
	@Autowired
	public ProjectController(ProjectService projectService) {
		this.projectService = projectService;
	}
	
	// 프로젝트 목록 조회
	@GetMapping("/project")
	public String projectList(
			@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "page", defaultValue = "1") int page,
			Model model) {
		
		int pageSize = 10;
		int offset = (page-1) * pageSize;
		
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
}
