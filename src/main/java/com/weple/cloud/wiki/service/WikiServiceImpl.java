package com.weple.cloud.wiki.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.weple.cloud.wiki.mapper.WikiMapper;

import lombok.RequiredArgsConstructor;

@Service@RequiredArgsConstructor
public class WikiServiceImpl implements WikiService {
	
	private final WikiMapper wikiMapper;
	
	// 위키 목록 조회 → 트리 구조로 변환
	@Override
	public List<WikiPageVO> findWikiTree(Long projectId) {
		List<WikiPageVO> flatList = wikiMapper.selectWikiList(projectId);
        return buildTree(flatList);
	}
	
	// 위키 단건 조회
	@Override
	public WikiPageVO findWikiById(String wikiPageId) {
		return wikiMapper.selectWikiById(wikiPageId);
	}
	
	// 위키 검색
	@Override
	public List<WikiPageVO> searchWiki(Long projectId, String keyword) {
		return wikiMapper.searchWiki(projectId, keyword);
	}
	
	// 위키 등록
	@Override
	@Transactional
	public int insertWiki(WikiPageVO vo) {
		return wikiMapper.insertWiki(vo);
	}
	
	// 위키 수정(프로시저로 버전 저장 후 업데이트)
	@Override
	@Transactional
	public int updateWiki(WikiPageVO vo) {
		// SP_SAVE_WIKI_VERSION 프로시저 호출은 MyBatis에서 처리
		return wikiMapper.updateWiki(vo);
	}
	
	// 위키 삭제
	@Override
	@Transactional
	public int deleteWiki(String wikiPageId) {
		return wikiMapper.deleteWiki(wikiPageId);
	}

	// 변경 이력 목록 조회
	@Override
	public List<WikiHistoryVO> findHistoryList(String wikiPageId) {
		return wikiMapper.selectHistoryList(wikiPageId);
	}

	// 특정 버전 조회
	@Override
	public WikiHistoryVO findHistoryByVersion(String wikiPageId, Integer wikiVersion) {
		return wikiMapper.selectHistoryByVersion(wikiPageId, wikiVersion);
	}
	
	// tree 구조 변환
	private List<WikiPageVO> buildTree(List<WikiPageVO> flatList) {
        Map<String, WikiPageVO> map = new LinkedHashMap<>();
        List<WikiPageVO> roots = new ArrayList<>();
 
        for (WikiPageVO vo : flatList) {
            vo.setChildren(new ArrayList<>());
            map.put(vo.getWikiPageId(), vo);
        }
 
        for (WikiPageVO vo : flatList) {
            String parentId = vo.getParentPageId();
            if (parentId == null || parentId.isBlank() || !map.containsKey(parentId)) {
                roots.add(vo);
            } else {
                map.get(parentId).getChildren().add(vo);
            }
        }
 
        return roots;
    }
	
	

}
