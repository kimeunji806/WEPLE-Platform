package com.weple.cloud.task.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.weple.cloud.history.task.service.TaskHistoryService;
import com.weple.cloud.task.mapper.TaskKanbanMapper;
import com.weple.cloud.task.mapper.TaskMapper;
import com.weple.cloud.task.service.TaskKanbanService;
import com.weple.cloud.task.service.TaskPermissionVO;
import com.weple.cloud.task.service.TaskStatusVO;
import com.weple.cloud.task.service.TaskVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskKanbanServiceImpl implements TaskKanbanService {

    private final TaskKanbanMapper taskKanbanMapper;
    private final TaskMapper taskMapper; // 기존 TaskMapper는 조회 용도로만 가져다 씀 (수정 없음)
    private final TaskHistoryService taskHistoryService; // 기존 작업내역(task_history) 저장 로직 재사용

    @Override
    public List<TaskVO> findKanbanList(Long pId) {
        return taskKanbanMapper.selectKanbanList(pId);
    }

    @Override
    @Transactional
    public int updateTaskStatus(String taskId, String taskStatus, String userCode, Long pId, boolean isAdminOrOwner) {

        TaskVO task = taskKanbanMapper.selectTaskManagerAndStatus(taskId);
        if (task == null) {
            throw new IllegalArgumentException("존재하지 않는 일감입니다.");
        }

        // 관리자/소유자는 담당자와 무관하게 상태 변경 자유롭게 허용
        if (!isAdminOrOwner) {
            TaskPermissionVO perms = taskMapper.checkTaskPermissions(userCode, pId);

            boolean canEditAll = perms != null && "k3_edit".equals(perms.getK3Edit());
            boolean canEditMine = perms != null && "k3_myedit".equals(perms.getK3Myedit())
                    && userCode != null && userCode.equals(task.getTaskManagerId());

            if (!canEditAll && !canEditMine) {
                throw new IllegalStateException("일감을 수정할 권한이 없습니다.");
            }
        }

        String beforeStatus = task.getTaskStatus();

        int result = taskKanbanMapper.updateTaskStatus(taskId, taskStatus);

        List<TaskStatusVO> statusList = taskMapper.taskStatuses();

        // insertHistory 파라미터를 인터페이스 이름 그대로 지역변수로 선언 - 개수/순서 실수 방지 (총 29개)
        String changedBy        = userCode;
        String actionType       = "UPDATE";
        String oldTitle         = null;
        String newTitle         = null;
        String oldTaskDescribe  = null;
        String newTaskDescribe  = null;
        String oldTypeName      = null;
        String newTypeName      = null;
        String oldStatus        = findStatusText(statusList, beforeStatus);
        String newStatus        = findStatusText(statusList, taskStatus);
        String oldManager       = null;
        String newManager       = null;
        String oldPriority      = null;
        String newPriority      = null;
        String oldStartDate     = null;
        String newStartDate     = null;
        String oldFinishDate    = null;
        String newFinishDate    = null;
        String oldEstimatedTime = null;
        String newEstimatedTime = null;
        String oldProgress      = null;
        String newProgress      = null;
        String oldParentTask    = null;
        String newParentTask    = null;
        String oldSpentHours    = null;
        String newSpentHours    = null;
        String oldFiles         = null;
        String newFiles         = null;

        taskHistoryService.insertHistory(
                taskId, changedBy, actionType,
                oldTitle, newTitle,
                oldTaskDescribe, newTaskDescribe,
                oldTypeName, newTypeName,
                oldStatus, newStatus,
                oldManager, newManager,
                oldPriority, newPriority,
                oldStartDate, newStartDate,
                oldFinishDate, newFinishDate,
                oldEstimatedTime, newEstimatedTime,
                oldProgress, newProgress,
                oldParentTask, newParentTask,
                oldSpentHours, newSpentHours,
                oldFiles, newFiles
        );

        return result;
    }
    private String findStatusText(List<TaskStatusVO> statusList, String commonId) {
        if (commonId == null) return null;
        for (TaskStatusVO s : statusList) {
            if (commonId.equals(s.getCommonId())) return s.getDefaultDescribe();
        }
        return commonId;
    }
}