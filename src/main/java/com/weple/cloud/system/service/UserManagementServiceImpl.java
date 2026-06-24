package com.weple.cloud.system.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.weple.cloud.system.mapper.UserManagementMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserManagementServiceImpl implements UserManagementService {

    private static final String ACTIVE_STATUS = "a2";
    private static final String INACTIVE_STATUS = "a3";

    private final UserManagementMapper userManagementMapper;

    @Override
    public List<UserManagementVO> findUsers(Long companyId, String keyword, int offset, int pageSize) {
        return userManagementMapper.selectUserManagementList(companyId, keyword, offset, pageSize);
    }

    @Override
    public int countUsers(Long companyId, String keyword) {
        return userManagementMapper.countUserManagementList(companyId, keyword);
    }

    @Override
    @Transactional
    public void changeUserStatus(Long companyId, int actorOwnerYn, String userCode, String status) {
        validateStatusChange(companyId, userCode, status);

        // 최고관리자는 관리자를 포함해 변경할 수 있고, 일반 관리자는 일반 사용자만 변경할 수 있습니다.
        if (userManagementMapper.updateUserStatus(companyId, actorOwnerYn, userCode, status) != 1) {
            throw new IllegalArgumentException("기업최고관리자만 다른 관리자의 상태를 변경할 수 있습니다.");
        }
    }

    private void validateStatusChange(Long companyId, String userCode, String status) {
        if (companyId == null) {
            throw new IllegalArgumentException("회사 정보가 없습니다.");
        }
        if (userCode == null || userCode.isBlank()) {
            throw new IllegalArgumentException("사용자 정보가 없습니다.");
        }
        if (!ACTIVE_STATUS.equals(status) && !INACTIVE_STATUS.equals(status)) {
            throw new IllegalArgumentException("활성 또는 비활성 상태만 변경할 수 있습니다.");
        }
    }
}
