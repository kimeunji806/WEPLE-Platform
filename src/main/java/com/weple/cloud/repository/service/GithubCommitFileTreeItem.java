package com.weple.cloud.repository.service;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GithubCommitFileTreeItem {

    private String name;
    private String path;
    private String type;
    private int level;
    private int indent;
    private GithubCommitFileChangeInfo file;
}
