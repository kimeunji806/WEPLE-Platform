package com.weple.cloud.repository.service;

import java.util.List;
import java.util.Map;

public interface RepositoryService {

    // 관리자가 입력한 GitHub 저장소를 프로젝트에 등록
    void registerRepository(RepositoryVO repository);

    // 기존 저장소의 표시 이름과 주 저장소 여부를 변경
    void updateRepository(RepositoryVO repository);

    // 확인용으로 입력한 저장소명이 일치할 때만 저장소를 삭제
    void deleteRepository(Long companyId, Long projectId, String repositoryId, String confirmationName);

    // 상단 저장소 탭을 노출할 수 있도록 프로젝트의 등록 저장소 존재 여부를 확인
    boolean hasRepository(Long projectId);

    // 현재 회사와 프로젝트에 등록된 저장소 목록을 조회
    List<RepositoryVO> findRepositories(Long companyId, Long projectId);

    // 목록에서 선택한 저장소 한 건을 조회
    RepositoryVO findRepository(Long companyId, Long projectId, String repositoryId);

    // 주 저장소 변경 확인 화면에 현재 지정된 저장소를 표시
    RepositoryVO findMainRepository(Long companyId, Long projectId);

    // 커밋 메시지에서 인식한 일감 코드에 표시할 일감 제목을 조회
    Map<String, String> findTaskTitles(Long projectId, List<String> taskIds);
}
