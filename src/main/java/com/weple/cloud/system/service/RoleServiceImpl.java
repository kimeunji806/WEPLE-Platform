package com.weple.cloud.system.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.weple.cloud.system.mapper.RoleMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
	private final RoleMapper roleMapper;
	
	@Override
	public List<RoleVO> selectRoleList() {
		return roleMapper.selectRoleList();
	}

}
