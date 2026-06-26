package com.weple.cloud.system.service;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserManagementProjectVO {

    // 사용자가 구성원으로 등록된 프로젝트 번호
    private Long projectId;

    // 상세조회 화면에 표시할 프로젝트명
    private String projectTitle;

    // 해당 프로젝트에서 사용자에게 연결된 역할명
    private String roleName;
}
