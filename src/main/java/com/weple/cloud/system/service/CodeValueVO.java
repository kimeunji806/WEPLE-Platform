package com.weple.cloud.system.service;

import java.util.Date;

public class CodeValueVO {
	// 코드값 내 기업
	private Integer companyId;
	private String companyCode;
	private String companyName;
	private Date createdAt;
	
	// 코드값 내 작업분류
	private Integer taskClassificationId;
	private String workName;
	private String defaultYn;
	private String usingYn;
	
	// 코드값 내 일감 우선순위
	private Integer taskPriorityId;
	private String priorityName;
}
