package com.weple.cloud.system.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.weple.cloud.system.mapper.CodeValueMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CodeValueServiceImpl implements CodeValueService {

	private final CodeValueMapper codeValueMapper;

	// -------------------------------코드값------------------------------
	// 전체조회
	@Override
	public List<CodeValueVO> findCodeValueAll() {
		return codeValueMapper.selectCodeValueAll();
	}

	// 등록
	@Override
	public int addCodeValue(CodeValueVO codeValueVO) {
		// TODO Auto-generated method stub
		return 0;
	}

	// 수정
	@Override
	public Map<String, Object> modifyCodeValue(CodeValueVO codeValueVO) {
		// TODO Auto-generated method stub
		return null;
	}

}
