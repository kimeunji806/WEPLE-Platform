package com.weple.cloud.history.worklog.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.weple.cloud.history.worklog.service.WorkLogVO;

@Mapper
public interface WorkLogMapper {
	public List<WorkLogVO> selectAll(
			@Param("projectId") String projectId,
	        @Param("startDate") String startDate,
	        @Param("endDate") String endDate,
	        @Param("userCode") String userCode,
	        @Param("typeNames") List<String> typeNames,
            @Param("offset") int offset,
            @Param("pageSize") int pageSize);
	
	public int countAll(
            @Param("projectId") String projectId,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("userCode") String userCode,
            @Param("typeNames") List<String> typeNames);
	
	public Double sumSpentHour(
			@Param("projectId")  String projectId,
		    @Param("startDate")  String startDate,
		    @Param("endDate")    String endDate,
		    @Param("userCode")   String userCode,
		    @Param("typeNames")  List<String> typeNames
			);
	
	// 날짜 목록 조회 (페이징용)
	public List<String> selectDistinctDates(
	    @Param("projectId") String projectId,
	    @Param("startDate") String startDate,
	    @Param("endDate")   String endDate,
	    @Param("userCode")  String userCode,
	    @Param("typeNames") List<String> typeNames
	);

	// 특정 날짜 전체 데이터 조회
	public List<WorkLogVO> selectByDate(
	    @Param("targetDate") String targetDate,
	    @Param("projectId")  String projectId,
	    @Param("userCode")   String userCode,
	    @Param("typeNames")  List<String> typeNames
	);
}
