package com.weple.cloud.repository.service;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GithubCommitInfo {
	// 해시코드
    private String sha;
    // GitHub 커밋 상세 URL 생성과 열람 상태 구분에 사용하는 전체 해시값
    private String fullSha;
    // 해시 코드와 커밋 메시지에 공통으로 연결하는 GitHub 커밋 상세 주소
    private String commitUrl;
    // 커밋 메세지
    private String message;
    // 커밋 메시지에서 자동 인식한 일감 코드
    private String taskId;
    // 자동 인식한 일감 코드와 매칭되는 일감 제목
    private String taskTitle;
    // 인식한 일감 코드가 현재 프로젝트의 task 테이블에 실제로 존재하는지 여부
    private boolean taskExists;
    // 이메일
    private String authorEmail;
    // 커밋 일시
    private String committedAt;
}
