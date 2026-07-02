package com.weple.cloud.time.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class WorkTimeVO {
	private long workId;
	private Long projectId;
	private String taskId;
	private String userCode;
	// DB work_name 타입 : NUMBER
	private long workName;
	//조인해서 가져온 실제 명칭 (String으로 변경)
	private String workNameLabel;
	@DateTimeFormat(pattern = "yyyy-MM-dd") 
	private Date workDate;
	private String spentContent;
	private Long spentHour;
	@DateTimeFormat(pattern = "yyyy-MM-dd") 
	private Date createdAt;
	@DateTimeFormat(pattern = "yyyy-MM-dd") 
	private Date updatedAt;
	
	private String projectTitle;
	private String taskTitle;
	private String userName; 
	private String taskDescribe;
	private Long countSpentHour;
	private Long totalSpentHour;
	private long taskClassificationId;
	private long estimatedTime;
	// 소요시간 등록 모달에서 함께 넘어오는 진척도 값 (미전송/잠금 시 null)
	private Long progress;
}