package com.weple.cloud.system.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.weple.cloud.system.service.SystemGroupUserVO;
import com.weple.cloud.system.service.SystemService;
import com.weple.cloud.system.service.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;
	private final SystemService systemService;
	
	// ---------------------------- 그룹 내 사용자 --------------------------
	// 전체조회
	@GetMapping("groupUserList")
	public String systemGroupUserList(@RequestParam(value = "groupId", required = false) Integer groupId, Model model) {
		List<SystemGroupUserVO> allList = userService.findGroupUserAll();
		List<SystemGroupUserVO> list;
		if (groupId != null) {
			list = allList.stream().filter(user -> user.getGroupId() != null && user.getGroupId().equals(groupId))
					.toList();
		} else {
			list = allList;
		}
		model.addAttribute("systemGroupUserList", list);
		model.addAttribute("currentGroupUsers", list);
		model.addAttribute("groupId", groupId);
		String groupName = "전체 사용자";
		if (groupId != null) {
			groupName = systemService.findGroupAll(null).stream()
					.filter(g -> g.getGroupId() != null
							&& String.valueOf(g.getGroupId()).equals(String.valueOf(groupId)))
					.map(g -> g.getGroupName()).findFirst().orElse("그룹명을 찾을 수 없음");
		}
		model.addAttribute("selectedGroupName", groupName);
		return "weple/admin/group/userList";
	}

	// 등록
	@GetMapping("groupUserInsert")
	public String groupUserInsertForm(@RequestParam(value = "groupId", required = false) Integer groupId, Model model) {
		List<SystemGroupUserVO> allList = userService.findGroupUserAll();
		List<SystemGroupUserVO> currentGroupUsers = new ArrayList<>();
		if (groupId != null) {
			currentGroupUsers = allList.stream()
					.filter(user -> user.getGroupId() != null && user.getGroupId().equals(groupId)).toList();
		}
		Set<String> currentUnitCodes = currentGroupUsers.stream().map(SystemGroupUserVO::getUserCode)
				.collect(Collectors.toSet());
		List<SystemGroupUserVO> availableUsers = allList.stream().filter(user -> user.getGroupId() == null).toList();
		model.addAttribute("currentGroupUsers", currentGroupUsers);
		model.addAttribute("availableUsers", availableUsers);
		 model.addAttribute("groupId", groupId);

		String groupName = "전체 사용자";
		if (groupId != null) {
			groupName = systemService.findGroupAll(null).stream()
					.filter(g -> g.getGroupId() != null
							&& String.valueOf(g.getGroupId()).equals(String.valueOf(groupId)))
					.map(g -> g.getGroupName()).findFirst().orElse("알 수 없는 그룹");
		}
		model.addAttribute("selectedGroupName", groupName);
		return "weple/admin/group/userInsert";
	}

	@PostMapping("groupUserInsert")
	public String groupUserInsertProcess(
	        @RequestParam("currentUserIds") List<String> currentUserIds,
	        @RequestParam Integer groupId,
	        HttpSession session) {

	    Integer companyId = (Integer) session.getAttribute("companyId"); // 또는 로그인 유저 정보

	    for (String userCode : currentUserIds) {
	        SystemGroupUserVO vo = new SystemGroupUserVO();
	        vo.setUserCode(userCode);
	        vo.setGroupId(groupId);
	        vo.setCompanyId(companyId); // ⭐ 이거 필수

	        userService.addGroupUser(vo);
	    }

	    return "redirect:/groupList";
	}

	// 수정
	@GetMapping("groupUserUpdate")
	public String groupUserUpdateForm(@RequestParam("userCode") String userCode, Model model) {
		List<SystemGroupUserVO> allUsers = userService.findGroupUserAll();
		SystemGroupUserVO findVO = allUsers.stream()
				.filter(user -> user.getUserCode() != null && user.getUserCode().equals(userCode)).findFirst()
				.orElse(null);
		model.addAttribute("groupUserUpdate", findVO);
		return "weple/admin/group/userInsert";
	}

	@PostMapping("groupUserUpdate")
	public String groupUserProcess(SystemGroupUserVO systemGroupUserVO) {
		userService.modefyGroupUser(systemGroupUserVO);
		return "redirect:/groupList";
	}

	// 삭제
	@GetMapping("groupUserDelete")
	public String groupUserDelete(@RequestParam String userCode) {
		userService.removeGroupUser(userCode);
		return "redirect:groupList";
	}
}
