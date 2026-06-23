package com.weple.cloud.task.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface TaskService {
	public List<TaskVO> findAll(Long pId);
	
	public List<TaskTypeListVO> findType(Long cId);
	
	public List<TaskStatusVO> findStatus();
	
	public List<TaskMemberVO> findMember(Long pId);
	
	public List<TaskPriorityVO> findPriority(Long cId);
	
	public List<TaskParentVO> findParent(Long pId);
	
	public List<TaskMilestoneVO> findMilestone(Long pId);
	
	public int insertTask(TaskVO taskVO , List<MultipartFile> files)throws Exception;
	
	public TaskVO findTaskDetail(String tId);
	
	public List<TaskVO> findChildTask(String tId);
	
	public List<TaskVO> findAllList(String tManager);
	
	public List<TaskProjectSelectVO> findMyProject(String uCode);
	
	public void updateTask(TaskVO taskVO, List<MultipartFile> files) throws Exception;
	
	public void deleteTask(String tId);
}
