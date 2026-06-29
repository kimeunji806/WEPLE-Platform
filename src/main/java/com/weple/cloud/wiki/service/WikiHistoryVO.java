package com.weple.cloud.wiki.service;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class WikiHistoryVO {
	private String wikiHistoryId;
    private String wikiPageId;
    private Integer wikiVersion;
    private String title;
    private String content;
    private String userCode;
    private LocalDateTime createdAt;
    
 // 조회용 추가 필드
    private String userName; // 수정자 이름
}
