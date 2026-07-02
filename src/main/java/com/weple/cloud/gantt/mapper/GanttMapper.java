package com.weple.cloud.gantt.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.weple.cloud.milestone.service.MilestoneInfoVO;
import com.weple.cloud.task.service.TaskVO;

public interface GanttMapper {
	List<TaskVO> selectTaskAll(@Param("projectId") Long projectId);
	
	// 집계 없는 마일스톤 조회
    List<MilestoneInfoVO> selectMilestoneForGantt(@Param("projectId") Long projectId);
}
