package com.weple.cloud.system.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.weple.cloud.system.service.SystemProjectVO;

@Mapper
public interface SystemProjectMapper {
	long selectMaxProjectId();
	
	// 프로젝트 기본 정보 등록
	public int insertInfo(SystemProjectVO systemProjectVO);
	// 프로젝트 모듈 매핑 데이터 일괄 등록
	public int insertModuleMapping(SystemProjectVO systemProjectVO);
}
