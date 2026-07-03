package com.weple.cloud.task.web;

import java.util.List;

import lombok.Data;

@Data
public class TaskKanbanBatchRequest {

    private Long projectId;
    private List<Item> changes;

    @Data
    public static class Item {
        private String taskId;
        private String taskStatus;
    }
}