package com.weple.cloud.project.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProjectMemberSearchVO {
    private Long projectId;   // 프로젝트 ID
    private String keyword;     // 사용자 검색 키워드 (이름 포함)
    private Long companyId;   // 같은 회사 사용자만 검색하기 위한 필터
}