package com.weple.cloud.milestone.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.weple.cloud.milestone.mapper.MilestoneMapper;
import com.weple.cloud.milestone.service.MilestoneListVO;
import com.weple.cloud.milestone.service.MilestoneService;
import com.weple.cloud.milestone.service.MilestoneVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MilestoneServiceImpl implements MilestoneService {

	private final MilestoneMapper milestoneMapper;
	
	@Override
	public List<MilestoneListVO> selectMilestoneAll(Long projectId) {
		return milestoneMapper.selectMilestoneAll(projectId);
	}

	@Override
	public MilestoneVO selectMilestoneById(Long milestoneId) {
		return milestoneMapper.selectMilestoneById(milestoneId);
	}

	@Override
	public int addMilestone(MilestoneVO milestoneVO) {
		int result = milestoneMapper.insertMilestone(milestoneVO);
		// 성공 시 1, 실패 시 -1 반환
		return result == 1 ? 1 : -1;
	}

	@Override
	public void updateMilestone(MilestoneVO milestoneVO) {
		milestoneMapper.updateMilestone(milestoneVO);
	}

	@Override
	public int deleteMilestone(Long milestoneId) {
		return milestoneMapper.deleteMilestone(milestoneId);
	}

}
