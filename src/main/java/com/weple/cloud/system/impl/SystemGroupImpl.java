package com.weple.cloud.system.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.weple.cloud.system.mapper.SystemGroupMapper;
import com.weple.cloud.system.service.SystemGroupService;
import com.weple.cloud.system.service.SystemGroupVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SystemGroupImpl implements SystemGroupService{
	
	private final SystemGroupMapper systemGroupMapper;
	
	@Override
	public List<SystemGroupVO> findGroupAll() {
		return null;
	}

	@Override
	public SystemGroupVO findGroupInfo(SystemGroupVO systemGroupVO) {
		return null;
	}

	@Override
	public int addGroup(SystemGroupVO systemGroupVO) {
		int result = systemGroupMapper.insertGroup(systemGroupVO);
		return result == 1 ? systemGroupVO.getGroupId() : -1;
	}

	@Override
	public Map<String, Object> modifyGroup(SystemGroupVO systemGroupVO) {
		return null;
	}

	@Override
	public Map<String, Object> remove(int groupId) {
		return null;
	}

}
