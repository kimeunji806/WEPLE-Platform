package com.weple.cloud.milestone.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.weple.cloud.auth.service.LoginUserDetails;
import com.weple.cloud.milestone.service.MilestoneListVO;
import com.weple.cloud.milestone.service.MilestoneService;
import com.weple.cloud.milestone.service.MilestoneVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/project/milestone") 
public class MilestoneController {

	private final MilestoneService milestoneService;

	// 마일스톤 전체 조회
	@GetMapping
	public String milestoneList(@RequestParam("projectId") Long projectId, Model model) {
		List<MilestoneListVO> list = milestoneService.selectMilestoneAll(projectId); 
		
		model.addAttribute("currentMenu", "milestone");
		model.addAttribute("projectId", projectId); 
		model.addAttribute("milestones", list);
		
		return "weple/milestone/list"; 
	}

	// 등록 페이지 조회
	@GetMapping("/insert")
	public String milestoneInsertForm(@RequestParam("projectId") Long projectId, Model model) {
		model.addAttribute("currentMenu", "milestone");
		model.addAttribute("projectId", projectId);
		
		return "weple/milestone/register";
	}

	// 등록하기 
	@PostMapping("/insert")
	public String milestoneInsert(
			@RequestParam("projectId") Long projectId,
			@AuthenticationPrincipal LoginUserDetails loginUser, 
			MilestoneVO milestoneVO) {
		
		String userCode = loginUser.getLoginUser().getUserCode();
		milestoneVO.setUserCode(userCode);
		milestoneVO.setProjectId(projectId);
		
		if (milestoneVO.getMilestoneStatus() == null) {
			milestoneVO.setMilestoneStatus("g1"); 
		}
		
		milestoneService.addMilestone(milestoneVO);
		
		return "redirect:/project/milestone?projectId=" + projectId;
	}

	// 수정 페이지 조회
	@GetMapping("/update")
	public String milestoneUpdateForm(
			@RequestParam("projectId") Long projectId,
			@RequestParam("milestoneId") Long milestoneId, 
			Model model) {
		
		MilestoneVO milestone = milestoneService.selectMilestoneById(milestoneId);
		
		model.addAttribute("currentMenu", "milestone");
		model.addAttribute("projectId", projectId);
		model.addAttribute("milestone", milestone);
		
		return "weple/milestone/register";
	}

	// 수정하기
	@PostMapping("/update")
	public String milestoneUpdate(@RequestParam("projectId") Long projectId, MilestoneVO milestoneVO) {
		milestoneVO.setProjectId(projectId);
		milestoneService.updateMilestone(milestoneVO);
		
		return "redirect:/project/milestone?projectId=" + projectId;
	}

	// 삭제하기 
	@PostMapping("/delete/{milestoneId}")
	@ResponseBody
	public ResponseEntity<String> milestoneDelete(@PathVariable("milestoneId") Long milestoneId) {
		int result = milestoneService.deleteMilestone(milestoneId);
		if (result > 0) {
			return ResponseEntity.ok("SUCCESS");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("FAIL");
		}
	}
}
