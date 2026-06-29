package com.weple.cloud.wiki.web;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.weple.cloud.auth.service.LoginUserDetails;
import com.weple.cloud.project.service.ProjectService;
import com.weple.cloud.wiki.service.WikiHistoryVO;
import com.weple.cloud.wiki.service.WikiPageVO;
import com.weple.cloud.wiki.service.WikiService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class WikiController {
	
	private final WikiService wikiService;
    private final ProjectService projectService;
    
    // 위키 조회 페이지(목록+선택된 문서)dd
    @GetMapping("/project/wiki")
    public String wikiPage(
            @RequestParam Long projectId,
            @RequestParam(required = false) String wikiPageId,
            @RequestParam(required = false) String keyword,
            Model model) {
    	
    	// 위키 트리 목록
    	List<WikiPageVO> wikiTree = wikiService.findWikiTree(projectId);
    	
    	// 선택된 문서
    	WikiPageVO selectedWiki = null;
    	List<WikiHistoryVO> historyList = null;
    	
    	if (wikiPageId != null && !wikiPageId.isBlank()) {
            selectedWiki = wikiService.findWikiById(wikiPageId);
            historyList  = wikiService.findHistoryList(wikiPageId);
        } else if (!wikiTree.isEmpty()) {
            // 기본: 첫 번째 문서 선택
            selectedWiki = wikiService.findWikiById(wikiTree.get(0).getWikiPageId());
            if (selectedWiki != null) {
                historyList = wikiService.findHistoryList(selectedWiki.getWikiPageId());
            }
        }
    	
    	// 검색어 있으면 검색 결과로 교체
        List<WikiPageVO> searchResult = null;
        if (keyword != null && !keyword.isBlank()) {
            searchResult = wikiService.searchWiki(projectId, keyword);
        }
 
        model.addAttribute("wikiTree", wikiTree);
        model.addAttribute("selectedWiki", selectedWiki);
        model.addAttribute("historyList", historyList);
        model.addAttribute("searchResult", searchResult);
        model.addAttribute("keyword", keyword);
        model.addAttribute("projectId", projectId);
        model.addAttribute("project", projectService.findById(String.valueOf(projectId)));
        model.addAttribute("moduleNames", projectService.findActiveModuleNames(projectId));
        
        model.addAttribute("sidebarMenu", "project");
        model.addAttribute("currentMenu", "wiki");
 
        return "weple/wiki/detail";
    }
    
    
    // 위키 등록 페이지
    @GetMapping("/project/wiki/register")
    public String wikiRegisterForm(
            @RequestParam Long projectId,
            @RequestParam(required = false) String parentPageId,
            Model model) {
 
        List<WikiPageVO> wikiTree = wikiService.findWikiTree(projectId);
 
        model.addAttribute("wikiTree", wikiTree);
        model.addAttribute("parentPageId", parentPageId);
        model.addAttribute("projectId", projectId);
        model.addAttribute("project", projectService.findById(String.valueOf(projectId)));
        model.addAttribute("moduleNames", projectService.findActiveModuleNames(projectId));
        model.addAttribute("sidebarMenu", "project");
        model.addAttribute("currentMenu", "wiki");
 
        return "weple/wiki/register";
    }
    
    // 위키 등록 처리
    @PostMapping("/project/wiki/register")
    public String wikiRegisterProcess(
            @RequestParam Long projectId,
            WikiPageVO vo,
            @AuthenticationPrincipal LoginUserDetails loginUser) {
 
        vo.setProjectId(projectId);
        vo.setUserCode(loginUser.getLoginUser().getUserCode());
        wikiService.insertWiki(vo);
 
        return "redirect:/project/wiki?projectId=" + projectId;
    }
    
    // 위키 수정 페이지
    @GetMapping("/project/wiki/modify/{wikiPageId}")
    public String wikiModifyForm(
            @PathVariable String wikiPageId,
            @RequestParam Long projectId,
            Model model) {
 
        WikiPageVO wiki = wikiService.findWikiById(wikiPageId);
        List<WikiPageVO> wikiTree = wikiService.findWikiTree(projectId);
 
        model.addAttribute("wiki", wiki);
        model.addAttribute("wikiTree", wikiTree);
        model.addAttribute("projectId", projectId);
        model.addAttribute("project", projectService.findById(String.valueOf(projectId)));
        model.addAttribute("moduleNames", projectService.findActiveModuleNames(projectId));
        model.addAttribute("sidebarMenu", "project");
        model.addAttribute("currentMenu", "wiki");
 
        return "weple/wiki/modify";
    }
    
    // 위키 수정 처리
    @PostMapping("/project/wiki/modify")
    public String wikiModifyProcess(
            @RequestParam Long projectId,
            WikiPageVO vo,
            @AuthenticationPrincipal LoginUserDetails loginUser) {
 
        vo.setProjectId(projectId);
        vo.setUserCode(loginUser.getLoginUser().getUserCode());
        wikiService.updateWiki(vo);
 
        return "redirect:/project/wiki?projectId=" + projectId + "&wikiPageId=" + vo.getWikiPageId();
    }
    
    // 위키 삭제
    @PostMapping("/project/wiki/delete/{wikiPageId}")
    public String wikiDelete(
            @PathVariable String wikiPageId,
            @RequestParam Long projectId) {
 
        wikiService.deleteWiki(wikiPageId);
        return "redirect:/project/wiki?projectId=" + projectId;
    }
    
    // 특정 버전 조회(AJAX)
    @GetMapping("/project/wiki/history")
    @ResponseBody
    public ResponseEntity<WikiHistoryVO> getHistoryVersion(
            @RequestParam String wikiPageId,
            @RequestParam Integer wikiVersion) {
 
        WikiHistoryVO history = wikiService.findHistoryByVersion(wikiPageId, wikiVersion);
        if (history == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(history);
    }

}
