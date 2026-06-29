package com.weple.cloud.repository.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GithubPatchLine {

    private String type;
    private String content;
}
