package com.weple.cloud.system.service;

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
public class SystemGroupVO {
	private Integer groupId;
	private Integer companyId;
	private String groupName;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createdAt;
}
