package com.weple.cloud.system.mapper;

import java.util.List;

import com.weple.cloud.system.service.SystemGroupVO;

public interface GroupMapper {
	// ---------------------------- 그룹 종류 --------------------------
	// 전체조회
	public List<SystemGroupVO> selectGroupAll(String keyword);

	// 등록
	public int insertGroup(SystemGroupVO systemGroupVO);

	// 삭제
	public int deleteGroup(int groupId);
}
