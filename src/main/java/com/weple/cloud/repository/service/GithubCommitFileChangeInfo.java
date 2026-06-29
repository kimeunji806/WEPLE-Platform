package com.weple.cloud.repository.service;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GithubCommitFileChangeInfo {

    private String filename;
    private String previousFilename;
    private String anchorId;
    private String status;
    private String statusLabel;
    private int additions;
    private int deletions;
    private int changes;
    private String patch;
    private List<GithubPatchLine> patchLines;
}
