package com.weple.cloud.system.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.weple.cloud.system.service.SystemGroupService;
import com.weple.cloud.system.service.SystemGroupVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SystemGroupController {
	
	private final SystemGroupService systemGroupService;
	
	//전체조회
	
	//상세조회
	
	//등록
	@GetMapping("groupInsert")
	public String groupInsertForm() {
		return "group/insert";
	}
	
	@PostMapping("groupInsert")
	public String groupInsertProcess(SystemGroupVO systemGroupVO) {
		int gno = systemGroupService.addGroup(systemGroupVO);
		return "redirect:groupInfo?gno=" + gno;
	}
	
	//수정
	
	//삭제

}
