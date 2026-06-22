package com.weple.cloud.system.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.weple.cloud.system.mapper.GroupMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

	private final GroupMapper groupMapper;

	// -------------------------------그룹 종류------------------------------
	// 그룹 전체조회
	@Override
	public List<SystemGroupVO> findGroupAll(String keyword) {
		return groupMapper.selectGroupAll(keyword);
	}

	// 그룹 등록
	@Override
	public int addGroup(SystemGroupVO systemGroupVO) {
		// 임시 테스트용 회사 ID
		systemGroupVO.setCompanyId(1);

		int result = groupMapper.insertGroup(systemGroupVO);
		return result == 1 ? systemGroupVO.getGroupId() : -1;
	}

	// 그룹 삭제
	@Override
	public Map<String, Object> removeGroup(int groupId) {
		Map<String, Object> map = new HashMap<>();
		int result = groupMapper.deleteGroup(groupId);
		if (result >= 1) {
			map.put("groupId", groupId);
		}
		return map;
	}

}
