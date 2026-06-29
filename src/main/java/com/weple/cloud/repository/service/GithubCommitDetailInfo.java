package com.weple.cloud.repository.service;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GithubCommitDetailInfo {

    private String sha;			// 해시코드
    private String shortSha;	// 쇼트 해시
    private String message;		// 커밋 메세지			
    private String authorEmail;	// 이메일
    private String committedAt;	// 커밋 시점
    private String commitUrl;	//커밋 url
    private int totalAdditions;	// 추가 행 정보
    private int totalDeletions;	// 삭제 행 정보
    private int totalChanges;	// 총합 변환
    private List<GithubCommitFileChangeInfo> files; // 개별 파일
    private List<GithubCommitFileTreeItem> fileTree; // 파일트리
}
