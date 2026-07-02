package com.weple.cloud.project.service;

import java.util.List;
import java.util.Set;

public interface ProjectMemberService {

	public List<ProjectMemberVO> findMemberList(Long projectId);

	public List<ProjectMemberVO> searchUsersForAdd(Long projectId, String keyword, Long companyId);
	
	public List<ProjectMemberVO> findGroupList(Long companyId);
	 
	public List<ProjectMemberVO> findUsersByGroupId(Long groupId, Long projectId);

	public List<ProjectMemberRoleVO> findRoleList();

	public int addMember(ProjectMemberVO vo);

	public int removeMember(Long memberId, Long projectId);
	
	public Set<String> findProjectPermissionCodes(String userCode, Long projectId);
	    boolean isMember(String userCode, Long projectId);
}