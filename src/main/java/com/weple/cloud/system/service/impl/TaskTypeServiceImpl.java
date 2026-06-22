package com.weple.cloud.system.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.weple.cloud.system.mapper.TaskTypeMapper;
import com.weple.cloud.system.service.TaskTypeService;
import com.weple.cloud.system.service.TaskTypeVO;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class TaskTypeServiceImpl implements TaskTypeService {

	private final TaskTypeMapper taskTypeMapper;
	
	@Override
	public List<TaskTypeVO> findTaskTypeAll(Long companyId) {
		return taskTypeMapper.selectTaskTypeAll(companyId);
	}
	
	@Override
	public TaskTypeVO findTaskTypeById(int typeId) {
		return taskTypeMapper.selectTaskTypeById(typeId);
	}

	@Override
	public void addTaskType(TaskTypeVO taskTypeVO) {
		taskTypeMapper.insertTaskType(taskTypeVO);

	}

	@Override
	@Transactional
	public void reorderTaskTypes(List<Integer> sortedIds) {
		for (int i = 0; i < sortedIds.size(); i++) {
			int position = i + 1;
			taskTypeMapper.updatePosition(sortedIds.get(i), position);
		}

	}

	@Override
	public void updateTaskType(TaskTypeVO taskTypeVO) {
		taskTypeMapper.updateTaskType(taskTypeVO);

	}

	@Override
	public int deleteTaskType(int typeId) {
		return taskTypeMapper.deleteTaskType(typeId);
	}

	

}




