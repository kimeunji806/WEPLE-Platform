package com.weple.cloud.system.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.weple.cloud.system.mapper.SystemProjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SystemProjectServiceImpl implements SystemProjectService {
	private final SystemProjectMapper systemProjectMapper;
	
	@Override
	@Transactional
	public int createProject(SystemProjectVO projectVO) {
		int result = systemProjectMapper.insertInfo(projectVO);
		
		// 프로젝트 등록 성공 시 선택된 모듈 리스트 매핑 등록
		if(result > 0) {
			List<String> moduleNames = projectVO.getModuleNames();
			
			if(moduleNames!= null && !moduleNames.isEmpty()) {
				systemProjectMapper.insertModuleMapping(projectVO);
			}
		}
		return result;
	}

	@Override
	public boolean existsByIdentifier(String projectIdentifier) {
		return systemProjectMapper.countByIdentifier(projectIdentifier) > 0;
	}

	@Override
	@Transactional
	public int deleteProject(String projectId) {
		return systemProjectMapper.deleteProject(projectId);
	}

	@Override
	public List<SystemProjectVO> selectProjectList(SystemProjectVO vo) {
		vo.setOffset((vo.getPage() - 1) * vo.getPageSize());
	    return systemProjectMapper.selectProjectList(vo);
	}

	@Override
	public int selectProjectCount(SystemProjectVO vo) {
		return systemProjectMapper.selectProjectCount(vo);
	}
	
}
