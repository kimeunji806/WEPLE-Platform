package com.weple.cloud.task.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.weple.cloud.file.TaskFileVO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class TaskVO {
    private String taskId;        
    private String taskTitle;
    private String taskDescribe;   
    private String taskStatus;    
    private String priority;       
    private String taskManager;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd") // 수신용 어노테이션
    @JsonFormat(pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
    private LocalDate startDate;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd") //수신용 어노테이션
    @JsonFormat(pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
    private LocalDate finishDate;  
    
    private Long estimatedTime; 
    private Long taskProgress;
    private String parentTaskId; 
    private Long spentHoursSum;
    
    @JsonFormat(pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
    private LocalDateTime updatedAt; 
    private Long milestoneId; 
    private Long projectId;   
    private String userCode; 
    private Long typeId;
    
    // 조인용 필드
    private String projectTitle;
    private String typeName;
    private String defaultDescribe;
    
    // 등록시 필요한 필드
    private String typeIdName;      // 화면 드롭다운에서 선택된 일감 유형 한글 이름
    private String parentTaskTitle; // 화면 자동완성 검색창에 입력된 상위일감 한글 제목
    
    // 상세조회시 필요 필드
    private String taskWriter;    // 일감 등록자 이름 
    private String milestoneTitle;
    
    
    private List<TaskFileVO> fileList; // 파일 크기 + 제목 목록
    
    private String taskStatusId;
    private String taskManagerId;
    
    private String taskManagerProfileImage;

}
