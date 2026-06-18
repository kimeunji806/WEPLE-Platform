package com.weple.cloud.system.service;

import java.util.List;
import java.util.Map;

public interface SystemGroupService {
	//전체조회
		public List<SystemGroupVO> findGroupAll();
		
		//상세조회
		public SystemGroupVO findGroupInfo(SystemGroupVO systemGroupVO);
		
		//등록
		public int addGroup(SystemGroupVO systemGroupVO);
		
		//수정
		public Map<String, Object> modifyGroup(SystemGroupVO systemGroupVO);
		
		//삭제
		public Map<String, Object> remove(int groupId);
}
