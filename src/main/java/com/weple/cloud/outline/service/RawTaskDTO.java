package com.weple.cloud.outline.service;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RawTaskDTO {
    private String taskStatus;
    private String priority;
    private String typeName;
    private String managerName;
    private double estimatedTime;
    private double spentHoursSum;
}
