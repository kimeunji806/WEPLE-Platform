package com.weple.cloud.system.service;

import java.util.List;

public interface SignupApprovalService {

    // 현재 관리자가 속한 회사의 승인 대기 회원을 요청한 페이지 범위만 조회합니다.
    List<SignupApprovalUserVO> findPendingUsers(Long companyId, int offset, int pageSize);

    // 가입승인 목록의 전체 페이지 수 계산에 사용할 승인 대기 회원 수를 조회합니다.
    int countPendingUsers(Long companyId);

    // 승인 대기 회원을 활성 계정으로 변경합니다.
    void approvePendingUser(Long companyId, String userCode);

    // 가입 요청을 취소하고 승인 대기 회원 정보를 삭제합니다.
    void cancelPendingUser(Long companyId, String userCode);
}
