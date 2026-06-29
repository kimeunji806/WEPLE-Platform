package com.weple.cloud.repository.service;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RepositoryTaskLinkInfo {

    // 커밋 메시지에서 인식한 일감 코드와 매칭되는 task_id입니다.
    private String taskId;

    // 커밋 목록에서 일감 코드를 눌러보기 전에 내용을 알 수 있도록 표시할 일감 제목입니다.
    private String taskTitle;
}
