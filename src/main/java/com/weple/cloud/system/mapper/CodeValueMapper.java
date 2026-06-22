package com.weple.cloud.system.mapper;

import java.util.List;

import com.weple.cloud.system.service.CodeValueVO;

public interface CodeValueMapper {
	// -------------------------------코드값------------------------------
	// 전체조회
	public List<CodeValueVO> selectCodeValueAll();

	// 등록
	public int insertCodeValue(CodeValueVO codeValueVO);

	// 수정
	public int updateCodeValue(CodeValueVO codeValueVO);
}
