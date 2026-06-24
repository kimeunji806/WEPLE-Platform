package com.weple.cloud.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.weple.cloud.system.service.UserManagementVO;

@Mapper
public interface UserManagementMapper {

    // 가입승인 완료 후 활성 또는 비활성 상태인 같은 회사 사용자를 요청한 페이지 범위만 조회합니다.
    List<UserManagementVO> selectUserManagementList(@Param("companyId") Long companyId,
                                                     @Param("keyword") String keyword,
                                                     @Param("offset") int offset,
                                                     @Param("pageSize") int pageSize);

    // 사용자 관리 목록의 전체 페이지 수 계산에 사용할 사용자 수를 조회합니다.
    int countUserManagementList(@Param("companyId") Long companyId,
                                @Param("keyword") String keyword);

    // 같은 회사에 속한 활성 회원을 활성(a2) 또는 비활성(a3) 상태로 변경
    int updateUserStatus(@Param("companyId") Long companyId,
                         @Param("actorOwnerYn") int actorOwnerYn,
                         @Param("userCode") String userCode,
                         @Param("status") String status);
}
