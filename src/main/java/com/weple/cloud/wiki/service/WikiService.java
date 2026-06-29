package com.weple.cloud.wiki.service;

import java.util.List;

public interface WikiService {
	// 위키 목록 조회 (트리 구조)
	public List<WikiPageVO> findWikiTree(Long projectId);
 
    // 위키 단건 조회
	public WikiPageVO findWikiById(String wikiPageId);
 
    // 위키 검색
	public List<WikiPageVO> searchWiki(Long projectId, String keyword);
 
    // 위키 등록
	public int insertWiki(WikiPageVO vo);
 
    // 위키 수정 (버전 저장 프로시저 호출)
	public int updateWiki(WikiPageVO vo);
 
    // 위키 삭제
	public int deleteWiki(String wikiPageId);
 
    // 변경 이력 목록 조회
	public List<WikiHistoryVO> findHistoryList(String wikiPageId);
 
    // 특정 버전 조회
	public WikiHistoryVO findHistoryByVersion(String wikiPageId, Integer wikiVersion);
}
