package com.weple.cloud.system.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.weple.cloud.system.mapper.SignupApprovalMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SignupApprovalServiceImpl implements SignupApprovalService {

    private final SignupApprovalMapper signupApprovalMapper;

    @Override
    public List<SignupApprovalUserVO> findPendingUsers(Long companyId, int offset, int pageSize) {
        return signupApprovalMapper.selectPendingUsersByCompanyId(companyId, offset, pageSize);
    }

    @Override
    public int countPendingUsers(Long companyId) {
        return signupApprovalMapper.countPendingUsersByCompanyId(companyId);
    }

    @Override
    @Transactional
    public void approvePendingUser(Long companyId, String userCode) {
        // 승인 대기 상태이면서 현재 관리자와 같은 회사인 경우에만 승인합니다.
        if (signupApprovalMapper.approvePendingUser(companyId, userCode) != 1) {
            throw new IllegalArgumentException("승인할 수 없는 가입 요청입니다.");
        }
    }

    @Override
    @Transactional
    public void cancelPendingUser(Long companyId, String userCode) {
        // 승인 전 요청만 삭제하므로 활성화된 회원 정보에는 영향을 주지 않습니다.
        if (signupApprovalMapper.cancelPendingUser(companyId, userCode) != 1) {
            throw new IllegalArgumentException("취소할 수 없는 가입 요청입니다.");
        }
    }
}
