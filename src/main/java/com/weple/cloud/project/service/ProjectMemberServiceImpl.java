package com.weple.cloud.project.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.weple.cloud.project.mapper.ProjectMemberMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectMemberServiceImpl implements ProjectMemberService {

    private final ProjectMemberMapper memberMapper;

    @Override
    public List<ProjectMemberVO> findMemberList(Long projectId) {
        return memberMapper.selectMemberList(projectId);
    }

    @Override
    public List<ProjectMemberVO> searchUsersForAdd(Long projectId, String keyword, Long companyId) {
        ProjectMemberSearchVO searchVO = new ProjectMemberSearchVO();
        searchVO.setProjectId(projectId);
        searchVO.setKeyword(keyword);
        searchVO.setCompanyId(companyId);
        return memberMapper.searchUsersForAdd(searchVO);
    }

    @Override
    public List<ProjectMemberRoleVO> findRoleList() {
        return memberMapper.selectRoleList();
    }

    @Override
    @Transactional
    public int addMember(ProjectMemberVO vo) {
        int result = memberMapper.insertMember(vo);
        if (result > 0 && vo.getRoleId() != null) {
            memberMapper.insertMemberRole(vo.getMemberId(), vo.getRoleId());
        }
        return result;
    }

    @Override
    @Transactional
    public int removeMember(Long memberId, Long projectId) {
        memberMapper.deleteMemberRoles(memberId);
        return memberMapper.deleteMember(memberId, projectId);
    }

	@Override
	public List<ProjectMemberVO> findGroupList(Long companyId) {
		return memberMapper.selectGroupList(companyId);
	}

	@Override
	public List<ProjectMemberVO> findUsersByGroupId(Long groupId, Long projectId) {
		return memberMapper.selectUsersByGroupId(groupId, projectId);
	}

	@Override
	public Set<String> findProjectPermissionCodes(String userCode, Long projectId) {
		return new java.util.HashSet<>(memberMapper.selectProjectPermissionCodes(userCode, projectId));
	}

	@Override
	public boolean isMember(String userCode, Long projectId) {
		return memberMapper.isMember(userCode, projectId);
	}
}