package com.weple.cloud.project.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.weple.cloud.history.worklog.service.WorkLogVO;
import com.weple.cloud.project.mapper.ProjectWorkLogMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectWorkLogServiceImpl implements ProjectWorkLogService {
	private final ProjectWorkLogMapper projectWorkLogMapper;
	
	@Override
	public List<WorkLogVO> findAll(
			String projectId,
            String startDate,
            String endDate,
            String userCode,
            List<String> typeNames,
            int offset,
            int pageSize){
		
		return projectWorkLogMapper.selectProjectWorkLog(
				projectId, startDate, endDate, userCode, typeNames, offset, pageSize);
	}

	@Override
	public int countAll(String projectId, String startDate, String endDate, String userCode, List<String> typeNames) {
		return projectWorkLogMapper.countProjectWorkLog(
                projectId, startDate, endDate, userCode, typeNames);
	}

	@Override
	public Double sumSpentHour(String projectId, String startDate, String endDate, String userCode,
			List<String> typeNames) {
		return projectWorkLogMapper.sumSpentHour(projectId, startDate, endDate, userCode, typeNames);
	}

	@Override
	public List<String> findDistinctDates(String projectId, String startDate, String endDate, String userCode,
			List<String> typeNames) {
		return projectWorkLogMapper.selectDistinctDates(projectId, startDate, endDate, userCode, typeNames);
	}

	@Override
	public List<WorkLogVO> findByDate(String targetDate, String projectId, String userCode, List<String> typeNames) {
		return projectWorkLogMapper.selectByDate(targetDate, projectId, userCode, typeNames);
	}
}
