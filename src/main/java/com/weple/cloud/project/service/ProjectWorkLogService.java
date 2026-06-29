package com.weple.cloud.project.service;

import java.util.List;

import com.weple.cloud.history.worklog.service.WorkLogVO;

public interface ProjectWorkLogService {
	public List<WorkLogVO> findAll(
			String projectId,
            String startDate,
            String endDate,
            String userCode,
            List<String> typeNames,
            int offset,
            int pageSize);
	
	public int countAll(
	            String projectId,
	            String startDate,
	            String endDate,
	            String userCode,
	            List<String> typeNames);
	
	public Double sumSpentHour(
				  String projectId,
				  String startDate,
				  String endDate,
				  String userCode,
				  List<String> typeNames);
	
	public List<String> findDistinctDates(
            String projectId,
            String startDate,
            String endDate,
            String userCode,
            List<String> typeNames);
	
	public List<WorkLogVO> findByDate(
            String targetDate,
            String projectId,
            String userCode,
            List<String> typeNames);
}
