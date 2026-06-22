package com.weple.cloud.system.service;

import java.util.List;

public interface TaskTypeService {
	
	// 일감유형 전체조회
	public List<TaskTypeVO> findTaskTypeAll(Long companyId);
		
	// 일감유형 상세조회
	public TaskTypeVO findTaskTypeById(int typeId);
		
	// 일감유형 등록
	public void addTaskType(TaskTypeVO taskTypeVO);
		
	// 일감유형 순서 수정
	void reorderTaskTypes(List<Integer> sortedIds);
		
	// 일감유형 편집
	public void updateTaskType(TaskTypeVO taskTypeVO);
		
	// 일감유형 삭제
	public int deleteTaskType(int typeId);
}
