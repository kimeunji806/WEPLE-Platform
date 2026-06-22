package com.weple.cloud.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.weple.cloud.system.service.TaskTypeVO;

public interface TaskTypeMapper {

	// 전체 조회
	public List<TaskTypeVO> selectTaskTypeAll(Long companyId);
	
	// 상세 조회
	public TaskTypeVO selectTaskTypeById(Integer typeId);
	
	// 등록
	public int insertTaskType(TaskTypeVO taskType);
	
	// 순서 수정(드래그&드랍 결과)
	int updatePosition(@Param("typeId") Integer typeId, @Param("position") Integer position);
	
	// 편집
	public int updateTaskType(TaskTypeVO taskType);
	
	// 삭제
	public int deleteTaskType(Integer typeId);
	

}
