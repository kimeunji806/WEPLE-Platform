package com.weple.cloud.system.mapper;

import java.util.List;

import com.weple.cloud.system.service.SystemGroupVO;

public interface SystemGroupMapper {
	//전체조회
	public List<SystemGroupVO> selectGroupAll();
	
	//상세조회
	public SystemGroupVO selectGroupInfo(SystemGroupVO systemGroupVO);
	
	//등록
	public int insertGroup(SystemGroupVO systemGroupVO);
	
	//수정
	public int updateGroup(SystemGroupVO systemGroupVO);
	
	//삭제
	public int deleteGroup(int groupId);
}
