package com.weple.cloud.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.weple.cloud.system.service.RoleVO;

@Mapper
public interface RoleMapper {
	// 전체 목록 조회
	List<RoleVO> selectRoleList();
	
	// 단건 조회
	
	// 전체 권한 목록 조회
	
	// 역할에 매핑된 권한 코드 목록
	
	// 역할 등록
	
	// 역할-권한 매핑 등록
	
	// 역할-권한 매핑 전체 삭제(수정 시 초기화)
	
	// 역할 삭제
}
