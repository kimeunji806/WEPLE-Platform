package com.weple.cloud.file;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.weple.cloud.project.service.ProjectService;
import com.weple.cloud.project.service.ProjectVO;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/project/{projectId}/file")
public class ProjectFileController {
	
	private final ProjectFileService projectFileService;
	private final ProjectService projectService;
	
	// -------------------------------파일관리------------------------------
	// 전체조회
	@GetMapping({"", "/projectFileList"})
	public String projectFileList(@PathVariable String projectId, Model model) {
        List<ProjectFileVO> list = projectFileService.findProjectFileAll(projectId);
        ProjectVO project = projectService.findById(projectId);
        model.addAttribute("projectFileList", list);
        model.addAttribute("projectId", projectId);
        model.addAttribute("project", project);
        model.addAttribute("currentMenu", "file");
        model.addAttribute("sidebarMenu", "project");
        return "weple/file/list";
    }
		
	// 상세조회
	@GetMapping("/projectFileInfo")
	public String projectFileInfo(@PathVariable String projectId, String fileId, Model model) {
		ProjectFileVO projectFileInfoVO = projectFileService.findProjectFileInfo(fileId);
		List<ProjectFileVersionsVO> versionList = projectFileService.findProjectFileVersionAll(fileId);
		ProjectVO project = projectService.findById(projectId);
		model.addAttribute("projectFileInfo", projectFileInfoVO);
		model.addAttribute("projectFileVersionList", versionList);
		model.addAttribute("projectId", projectId);
		model.addAttribute("project", project);
		model.addAttribute("currentMenu", "file");
		model.addAttribute("sidebarMenu", "project");
		return "weple/file/detail";
	}
		
	// 등록
	@GetMapping("/projectFileInsert")
	public String projectFileInsertForm(@PathVariable String projectId) {
		return "weple/file/list";
	}
	
	@PostMapping("/projectFileInsert")
	public String projectFileInsertProcess(@PathVariable String projectId, ProjectFileVO projectFileVO) {
		String fno = projectFileService.addProjectFile(projectFileVO);
		return "redirect:projectFileInfo?fno=" + fno;
	}
		
	// 삭제
	@GetMapping("/deleteProjectFile")
	public String deleteProjectFile(@PathVariable String projectId, String fileId) {
		projectFileService.removeProjectFileVersionByFileId(fileId);
		projectFileService.removeProjectFile(fileId);
		return "redirect:/project/" + projectId + "/file";
	}
	
	// -------------------------------파일 버전------------------------------
  	// 전체조회
    @GetMapping("/downloader")
    public String downloader(@PathVariable String projectId, Model model) {
        ProjectVO project = projectService.findById(projectId);
        model.addAttribute("projectId", projectId);
        model.addAttribute("project", project);
        model.addAttribute("currentMenu", "file");
        model.addAttribute("sidebarMenu", "project");
        model.addAttribute("downloadHistoryList", projectFileService.findProjectFileAll(projectId));
        return "weple/file/downloader";
    }

    @GetMapping("/projectFileVersionList")
    public String projectFileVersionList(String fileId, Model model) {
    	List<ProjectFileVersionsVO> list = projectFileService.findProjectFileVersionAll(fileId);
    	model.addAttribute("projectFileVersionList", list);
    	return "weple/file/list";
    }
    
    // 상세조회
    @GetMapping("/projectFileVersionInfo")
    public String projectFileVersionInfo(@PathVariable String projectId, String versionId, Model model) {
        ProjectFileVersionsVO vo = projectFileService.findProjectFileVersionInfo(versionId);
        model.addAttribute("projectFileVersionInfo", vo);
        return "weple/file/versionDetail";
    }
    
    // 등록
    @GetMapping("/projectFileVersionInsert")
	public String projectFileVersionForm() {
		return "weple/file/list";
	}    
    
    @PostMapping("/projectFileVersionInsert")
    public String projectFileVersionProcess(ProjectFileVersionsVO projectFileVersionsVO) {
    	String fvno = projectFileService.addProjectFileVersion(projectFileVersionsVO);
    	return "redirect:projectFileVersionInfo?fvno=" + fvno;
    }
    
    // 삭제
    @GetMapping("/projectFileVersionDelete")
    public String projectFileVersionDelete(@PathVariable String projectId, String versionId) {
    	long result = projectFileService.removeProjectFileVersion(versionId);
    	return "redirect:projectFileVersionList?versionId=" + versionId;
    }
}