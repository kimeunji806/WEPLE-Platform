package com.weple.cloud.system.service;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class CodeValueVO {
	// 코드값 내 기업
	private long companyId;
	private String companyCode;
	private String companyName;
	private Date createdAt;
	
	// 코드값 내 작업분류
	private String taskClassificationId;
	private String workName;
	private String defaultYn;
	private String usingYn;
	
	// 코드값 내 일감 우선순위
	private String taskPriorityId;
	private String priorityName;
	private long orderNo;
}
