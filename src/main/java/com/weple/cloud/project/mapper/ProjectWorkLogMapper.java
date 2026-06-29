package com.weple.cloud.project.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.weple.cloud.history.worklog.service.WorkLogVO;

@Mapper
public interface ProjectWorkLogMapper {
	public List<WorkLogVO> selectProjectWorkLog(
			@Param("projectId") String projectId,
			@Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("userCode") String userCode,
            @Param("typeNames") List<String> typeNames,
            @Param("offset") int offset,
            @Param("pageSize") int pageSize);
		    
	public int countProjectWorkLog(
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
	
	// 날짜 목록 조회 (날짜 단위 페이징용)
    public List<String> selectDistinctDates(
            @Param("projectId") String projectId,
            @Param("startDate") String startDate,
            @Param("endDate")   String endDate,
            @Param("userCode")  String userCode,
            @Param("typeNames") List<String> typeNames);
 
    // 특정 날짜 전체 데이터 조회 (페이징 없이 스크롤)
    public List<WorkLogVO> selectByDate(
            @Param("targetDate") String targetDate,
            @Param("projectId")  String projectId,
            @Param("userCode")   String userCode,
            @Param("typeNames")  List<String> typeNames);
}
