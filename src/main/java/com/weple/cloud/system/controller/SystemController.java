package com.weple.cloud.system.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.weple.cloud.system.service.SystemGroupVO;
import com.weple.cloud.system.service.SystemService;
import com.weple.cloud.system.service.TaskTypeVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SystemController {

	private final SystemService systemService;

	// ---------------------------- 그룹 종류 --------------------------
	// 전체조회
	@GetMapping("groupList")
	public String systemGroupList(@RequestParam(required = false) String keyword, Model model) {
		List<SystemGroupVO> list = systemService.findGroupAll(keyword);
		model.addAttribute("systemGroupList", list);
		model.addAttribute("keyword", keyword);
		model.addAttribute("menu", "group");
		return "weple/admin/group/list";
	}

	// 등록
	@GetMapping("groupInsert")
	public String groupInsertForm() {
		return "weple/admin/group/insert";
	}

	@PostMapping("groupInsert")
	public String groupInsertProcess(SystemGroupVO systemGroupVO) {
		systemGroupVO.setCompanyId(1);
		int gno = systemService.addGroup(systemGroupVO);
		return "redirect:groupList";
	}

	// 삭제
	@GetMapping("groupDelete")
	public String groupDelete(Integer groupId) {
		systemService.removeGroup(groupId);
		return "redirect:groupList";
	}

	// -------------------------------일감유형------------------------------
	// 전체조회
	@GetMapping("/system/taskType")
	public String systemTaskTypeList(Model model) {
		List<TaskTypeVO> list = systemService.findTaskTypeAll();
		model.addAttribute("taskTypes", list);
		return "weple/system/taskType/list";
	}

	// 등록페이지 조회
	@GetMapping("/system/taskTypeInsert")
	public String taskTypeInsert() {
		return "weple/system/taskType/register";
	}

	// 등록하기
	@PostMapping("/system/taskTypeInsert")
	public String systemTaskTypeInsert(TaskTypeVO taskTypeVO) {
		systemService.addTaskType(taskTypeVO);
		return "redirect:/system/taskType";
	}

	// 순서 수정하기 (드래그&드랍으로 변경된 순서)
	@PutMapping("/system/taskTypeReorder")
	@ResponseBody
	public ResponseEntity<String> systemTaskTypeReorder(@RequestBody List<Integer> sortedIds) {
		systemService.reorderTaskTypes(sortedIds);
		return ResponseEntity.ok("SUCCESS");
	}

	// 수정페이지 조회
	@GetMapping("/system/taskTypeUpdate")
	public String taskTypeUpdate() {
		return "weple/system/taskType/register";
	}

	// 수정하기
//	@PutMapping("/system/taskTypeUpdate")
//	public String

	
	// ---------------------------- 가입승인 --------------------------
	private final com.weple.cloud.system.service.SignupApprovalService signupApprovalService;

	// 현재 로그인한 관리자의 회사에 접수된 승인 대기 회원을 조회합니다.
	@GetMapping("/approvalList")
	public String approvalList(
			@org.springframework.security.core.annotation.AuthenticationPrincipal com.weple.cloud.auth.service.LoginUserDetails loginUser,
			Model model) {
		List<com.weple.cloud.system.service.SignupApprovalUserVO> pendingUsers =
				signupApprovalService.findPendingUsers(loginUser.getLoginUser().getCompanyId());
		model.addAttribute("pendingUsers", pendingUsers);
		model.addAttribute("menu", "approval");
		return "weple/admin/config/join-request";
	}

	// 요청 URL의 사용자 코드만 받고 회사 정보는 로그인 세션에서 가져와 승인합니다.
	@PostMapping("/approvalList/{userCode}/approve")
	public String approveSignupRequest(
			@org.springframework.security.core.annotation.AuthenticationPrincipal com.weple.cloud.auth.service.LoginUserDetails loginUser,
			@org.springframework.web.bind.annotation.PathVariable String userCode,
			org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
		try {
			signupApprovalService.approvePendingUser(loginUser.getLoginUser().getCompanyId(), userCode);
			redirectAttributes.addFlashAttribute("approvalSuccess", "가입 요청을 승인했습니다.");
		} catch (IllegalArgumentException ex) {
			redirectAttributes.addFlashAttribute("approvalError", ex.getMessage());
		}
		return "redirect:/approvalList";
	}

	// 취소한 승인 대기 가입 요청은 USERS 테이블에서 삭제합니다.
	@PostMapping("/approvalList/{userCode}/cancel")
	public String cancelSignupRequest(
			@org.springframework.security.core.annotation.AuthenticationPrincipal com.weple.cloud.auth.service.LoginUserDetails loginUser,
			@org.springframework.web.bind.annotation.PathVariable String userCode,
			org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
		try {
			signupApprovalService.cancelPendingUser(loginUser.getLoginUser().getCompanyId(), userCode);
			redirectAttributes.addFlashAttribute("approvalSuccess", "가입 요청을 취소했습니다.");
		} catch (IllegalArgumentException ex) {
			redirectAttributes.addFlashAttribute("approvalError", ex.getMessage());
		}
		return "redirect:/approvalList";
	}
	//-------------------------------프로젝트------------------------------
	// 프로젝트 생성
	
	
	
}
