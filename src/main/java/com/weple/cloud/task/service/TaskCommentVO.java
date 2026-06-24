package com.weple.cloud.task.service;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class TaskCommentVO {
	private Long commentId;
	private String taskId;
	private Long parentCommentId;
	private String userCode;
	private String userName;
	private String profileImage;
	private String taskComment;
	private LocalDate createdAt;
}
