package com.weple.cloud.history.worklog.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.weple.cloud.history.worklog.mapper.WorkLogMapper;
import com.weple.cloud.history.worklog.service.WorkLogService;
import com.weple.cloud.history.worklog.service.WorkLogVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WorkLogServiceImpl implements WorkLogService {
	private final WorkLogMapper workLogMapper;
	
	@Override
	public List<WorkLogVO> findAll(
			String projectId,
	        String startDate,
	        String endDate,
	        String userCode,
	        List<String> typeNames,
	        int offset,
            int pageSize){
		return workLogMapper.selectAll(
				projectId, startDate, endDate, userCode, typeNames, offset, pageSize);
	}

	@Override
	public int countAll(String projectId, String startDate, String endDate, String userCode, List<String> typeNames) {
		return workLogMapper.countAll(
                projectId, startDate, endDate, userCode, typeNames);
	}

	@Override
	public Double sumSpentHour(String projectId, String startDate, String endDate, String userCode,
			List<String> typeNames) {
		return workLogMapper.sumSpentHour(projectId, startDate, endDate, userCode, typeNames);
	}

	@Override
	public List<String> findDistinctDates(String projectId, String startDate, String endDate, String userCode,
			List<String> typeNames) {
		return workLogMapper.selectDistinctDates(
	            projectId, startDate, endDate, userCode, typeNames);
	}

	@Override
	public List<WorkLogVO> findByDate(String targetDate, String projectId, String userCode, List<String> typeNames) {
		return workLogMapper.selectByDate(
	            targetDate, projectId, userCode, typeNames);
	}
}
