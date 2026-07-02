package com.weple.cloud.milestone.service;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter 
@Setter
@ToString
public class TaskStatDTO {
    private Long milestoneId;
    private int totalTaskCount;
    private int closedTaskCount;
    private int progressPercentage;
}
