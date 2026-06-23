package com.weple.cloud.file;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class FileVO {
    private Long fileId;
    private String taskId;
    private String fileName;
    private String isDeleted; // 기본값 'N' 처리
    private LocalDateTime createdAt;
}