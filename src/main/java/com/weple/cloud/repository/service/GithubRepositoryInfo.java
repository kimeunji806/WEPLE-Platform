package com.weple.cloud.repository.service;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GithubRepositoryInfo {

    // GitHub 저장소 설명과 브랜치 선택에 필요한 기본 정보
    private String description;
    private String defaultBranch;
    private String selectedBranch;
    private List<String> branches;

    // 현재 조회 중인 폴더 경로와 상위 폴더 이동에 필요한 경로
    private String currentDirectory;
    private String parentDirectory;

    // 현재 폴더의 파일/폴더 목록과 사용자가 선택한 파일 내용
    private List<GithubFileInfo> files;
    private String selectedFilePath;
    private String selectedFileContent;

    // 선택한 브랜치의 커밋 목록
    private List<GithubCommitInfo> commits;

    // GitHub API 응답의 Link 헤더를 기준으로 계산한 커밋 목록 페이징 정보
    private int commitPage;
    private int totalCommitPages;
    private boolean hasNextCommitPage;
    private int startCommitPage;
    private int endCommitPage;
}
