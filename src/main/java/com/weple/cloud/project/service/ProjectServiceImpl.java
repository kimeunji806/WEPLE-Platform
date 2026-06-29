package com.weple.cloud.project.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.weple.cloud.project.mapper.ProjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
	
	private final ProjectMapper projectMapper;
	
	// 전체 목록 조회(페이징)
	@Override
	public List<ProjectVO> findAll(String keyword, int offset, int pageSize){
		return projectMapper.selectAll(keyword, offset, pageSize);
	}
	
	// 전체 목록 조회(페이징x)
	@Override
	public List<ProjectVO> findAll(String keyword) {
		return projectMapper.selectAllNoPage(keyword);
	}
	
	// 전체 건수 조회
	@Override
	public int countAll(String keyword) {
		return projectMapper.countAll(keyword);
	}
	
	// 단건 조회
	@Override
	public ProjectVO findById(String projectId) {
		return projectMapper.selectById(projectId);
	}

	// 관리에서 선택된 모듈 전체 목록 조회
	@Override
	public List<String> findModuleNames(Long projectId) {
		return projectMapper.selectModuleNames(projectId);
	}
	
	// 네비바 활성화된 모듈만 조회
	@Override
	public List<String> findActiveModuleNames(Long projectId) {
		return projectMapper.selectActiveModuleNames(projectId);
	}
	
	// 설정 페이지 - 프로젝트 설정 정보 조회
	@Override
	public ProjectVO findSettingById(Long projectId) {
		ProjectVO vo = projectMapper.selectSettingById(projectId);
		List<String> modules = projectMapper.selectModuleNames(projectId);
		vo.setModuleNames(modules != null ? modules : new ArrayList<>());
		return vo;
	}
	
	// 설정 페이지 - 프로젝트 설정 저장
    @Override
    @Transactional
    public void saveProjectSetting(ProjectVO vo) {
        // 기본 정보 수정(제목, 설명)
        projectMapper.updateProjectSetting(vo);
 
        //    관리 모듈 전체 목록을 기준으로 is_active만 업데이트
        List<String> checkedModules = vo.getModuleNames();
        if (checkedModules == null) checkedModules = new ArrayList<>();
 
        // 관리에서 선택된 전체 모듈 목록 조회
        List<String> allModules = projectMapper.selectModuleNames(vo.getProjectId());
 
        for (String moduleName : allModules) {
            // 체크된 모듈은 Y, 해제된 모듈은 N
            String isActive = checkedModules.contains(moduleName) ? "Y" : "N";
            projectMapper.updateModuleActive(vo.getProjectId(), moduleName, isActive);
        }
    }
	
	// URL 접근 제어 - module_mapping row 존재 여부로 활성화 판단
    @Override
    public boolean isModuleActive(Long projectId, String moduleName) {
        return projectMapper.isModuleActive(projectId, moduleName) > 0;
    }

}
