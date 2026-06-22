package com.weple.cloud.milestone.service;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class MilestoneVO {
	private Integer milestoneId;
	private Integer projectId;
	private String  loginId;
	private String  milestoneTitle;
	private String  milestoneDescribe;
	private Date    finishDate;
	private String  milestoneStatus;
	private Date    createdAt;
}
