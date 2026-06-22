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
public class SystemGroupUserVO {
	private String userCode;                 //NOT NULL
	private Integer companyId;               //NOT NULL
	private String loginId;                  //NOT NULL
	private String password;                  //NOT NULL
	private String userName;                 //NOT NULL
	private String email;                     //NOT NULL
	private String phoneNumber;              //NOT NULL
	private String approval;                  //CHECK
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createDate;
	private String status;                    //NOT NULL
	private String profileImage;
	private Integer ownerYn;                 //NOT NULL
	private Integer adminYn;
	private String webNotificationYn;
	private String emailNotificationYn;     //PK
	private String notificationArea;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date lastLoginTime;             //FK
	private Integer groupId;                 //FK
	
	private Integer roleId;
	private String roleName;
}
