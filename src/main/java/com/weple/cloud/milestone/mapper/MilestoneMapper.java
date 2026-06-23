package com.weple.cloud.milestone.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.weple.cloud.milestone.service.MilestoneListVO;
import com.weple.cloud.milestone.service.MilestoneVO;

public interface MilestoneMapper {
	
	// 전체 조회
	public List<MilestoneListVO> selectMilestoneAll(@Param("projectId")Long projectId);
	
	// 상세 조회
	public MilestoneVO selectMilestoneById(Long milestoneId);
	
	// 등록
	public int insertMilestone(MilestoneVO milestoneVO);
	
	// 수정
	public void updateMilestone(MilestoneVO milestoneVO);
	
	// 삭제
	public int deleteMilestone(Long milestoneId);
}
