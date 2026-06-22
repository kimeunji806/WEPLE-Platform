package com.weple.cloud.system.service;

import java.util.List;
import java.util.Map;

public interface CodeValueService {
	// -------------------------------코드값------------------------------
	// 전체조회
	public List<CodeValueVO> findCodeValueAll();

	// 등록
	public int addCodeValue(CodeValueVO codeValueVO);
	
	// 수정
	public Map<String, Object> modifyCodeValue(CodeValueVO codeValueVO);
}
