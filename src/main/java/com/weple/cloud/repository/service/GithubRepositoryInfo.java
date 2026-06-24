package com.weple.cloud.repository.service;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GithubRepositoryInfo {

    // GitHub 저장소의 설명과 기본 브랜치 정보
    private String description;
    private String defaultBranch;
    private String selectedBranch;
    private List<String> branches;
    private String currentDirectory;
    private String parentDirectory;
    private List<GithubFileInfo> files;
    private String selectedFilePath;
    private String selectedFileContent;
    private List<GithubCommitInfo> commits;

    // GitHub 커밋 목록 페이지 정보. GitHub API 응답의 Link 헤더를 기준으로 계산합니다.
    private int commitPage;
    private int totalCommitPages;
    private boolean hasNextCommitPage;
    private int startCommitPage;
    private int endCommitPage;
}
