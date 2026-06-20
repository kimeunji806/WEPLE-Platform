package com.weple.cloud.task.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.weple.cloud.task.mapper.TaskMapper;
import com.weple.cloud.task.service.TaskMemberVO;
import com.weple.cloud.task.service.TaskParentVO;
import com.weple.cloud.task.service.TaskPriorityVO;
import com.weple.cloud.task.service.TaskService;
import com.weple.cloud.task.service.TaskStatusVO;
import com.weple.cloud.task.service.TaskTypeListVO;
import com.weple.cloud.task.service.TaskVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
	
	private final TaskMapper taskMapper;
	
	@Override
	public List<TaskVO> findAll(Long pId) {
		return taskMapper.selectAll(pId);
	}

	@Override
	public List<TaskTypeListVO> findType(Long cId) {
		return taskMapper.taskTypes(cId);
	}

	@Override
	public List<TaskStatusVO> findStatus() {
		return taskMapper.taskStatuses();
	}

	@Override
	public List<TaskMemberVO> findMember(Long pId) {
		return taskMapper.taskMembers(pId);
	}
	@Override
	public List<TaskPriorityVO> findPriority(Long cId) {
		return taskMapper.taskPriorities(cId);
	}

	@Override
	public List<TaskParentVO> findParent(Long pId) {
		return taskMapper.taskParents(pId);
	}

	@Override
	@Transactional
    public int insertTask(TaskVO taskVO) {
        return taskMapper.insertTask(taskVO);
    }


}
