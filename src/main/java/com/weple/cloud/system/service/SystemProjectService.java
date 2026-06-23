package com.weple.cloud.system.service;

import java.util.List;

public interface SystemProjectService {
	public int createProject(SystemProjectVO projectVO);
	boolean existsByIdentifier(String projectIdentifier);
	
	List<SystemProjectVO> selectProjectList(SystemProjectVO vo);
	int selectProjectCount(SystemProjectVO vo);
	int deleteProject(String projectId);
}
