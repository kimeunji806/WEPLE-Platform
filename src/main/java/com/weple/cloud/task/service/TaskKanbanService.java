package com.weple.cloud.task.service;

import java.util.List;

public interface TaskKanbanService {

    List<TaskVO> findKanbanList(Long pId);

 // isAdminOrOwner: 관리자는 담당자 여부와 무관하게 상태 변경 가능해야 함
    int updateTaskStatus(String taskId, String taskStatus, String userCode, Long pId, boolean isAdminOrOwner);
}