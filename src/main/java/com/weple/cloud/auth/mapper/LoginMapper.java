package com.weple.cloud.auth.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.weple.cloud.auth.service.LoginUserVO;

@Mapper
public interface LoginMapper {


   // 로그인 아이디로 사용자 인증 정보를 조회
    LoginUserVO selectLoginUserByLoginId(@Param("loginId") String loginId);

   // 로그인 사용자의 세부 권한 코드 목록을 조회    
    List<String> selectPermissionCodesByUserCode(@Param("userCode") String userCode);

   // 로그인 성공 시 최근 로그인 일시를 현재 시각으로 갱신
    int updateLastLoginTime(@Param("userCode") String userCode);
}
