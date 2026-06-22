package com.weple.cloud.project.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.weple.cloud.project.mapper.ProjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
	private final ProjectMapper projectMapper;
	
	@Override
	public List<ProjectVO> findAll(String keyword){
		return projectMapper.selectAll(keyword);
	}

	@Override
	public ProjectVO findById(String projectId) {
		return projectMapper.selectById(projectId);
	}
}
