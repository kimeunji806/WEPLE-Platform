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
public class FileInfoVO {
    private Long versionId;
    private Long fileId;
    private Long versionNumber;
    private String filePath;
    private Long fileSize;
    private String uploader; // 유저 코드
    private LocalDateTime uploadedAt;
    private String savedName; // 실제 저장된 물리적 파일명 (UUID)
}