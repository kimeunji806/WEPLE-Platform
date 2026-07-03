package com.weple.cloud.task.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.weple.cloud.task.service.TaskVO;

// 칸반보드 전용 매퍼/기존 TaskMapper는 건드리지 않기 위해 분리
public interface TaskKanbanMapper {

    // 칸반보드용 일감 목록 (taskStatus, taskManagerId 포함)
    List<TaskVO> selectKanbanList(@Param("pId") Long pId);

    // 드래그로 상태만 변경
    int updateTaskStatus(@Param("taskId") String taskId, @Param("taskStatus") String taskStatus);
    
    TaskVO selectTaskManagerAndStatus(@Param("taskId") String taskId);
}