package com.weple.cloud.wiki.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.weple.cloud.wiki.service.WikiHistoryVO;
import com.weple.cloud.wiki.service.WikiPageVO;

@Mapper
public interface WikiMapper {
	// 프로젝트 위키 목록 조회(전체)
	public List<WikiPageVO> selectWikiList(@Param("projectId") Long projectId);
	
	// 위키 단건 조회
	public WikiPageVO selectWikiById(@Param("wikiPageId") String wikiPageId);
	
	// 위키 등록
	public int insertWiki(WikiPageVO vo);
	
	// 위키 수정(버전업은 프로시저 처리)
	public int updateWiki(WikiPageVO vo);
	
	// 위키 삭제
	public int deleteWiki(@Param("wikiPageId") String wikiPageId);
	
	// 위키 검색
	public List<WikiPageVO> searchWiki(
            @Param("projectId") Long projectId,
            @Param("keyword")   String keyword);
	
	// 변경 이력 목록 조회
	public List<WikiHistoryVO> selectHistoryList(@Param("wikiPageId") String wikiPageId);
	
	// 특정 버전 조회
	public WikiHistoryVO selectHistoryByVersion(
            @Param("wikiPageId")   String wikiPageId,
            @Param("wikiVersion")  Integer wikiVersion);
}
