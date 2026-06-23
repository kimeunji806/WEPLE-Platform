package com.weple.cloud.milestone.service;

import java.util.List;

public interface MilestoneService {

	// 전체 조회
	public List<MilestoneListVO> selectMilestoneAll(Long projectId);
	
	// 상세 조회
	public MilestoneVO selectMilestoneById(Long milestoneId);
	
	// 등록
	public int addMilestone(MilestoneVO milestoneVO);
	
	// 수정
	public void updateMilestone(MilestoneVO milestoneVO);
	
	// 삭제
	public int deleteMilestone(Long milestoneId);
}
