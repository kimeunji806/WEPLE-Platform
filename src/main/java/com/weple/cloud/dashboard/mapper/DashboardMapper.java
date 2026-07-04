package com.weple.cloud.dashboard.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.weple.cloud.task.service.TaskVO;

@Mapper
public interface DashboardMapper {
    List<TaskVO> selectTasksDueWithinAWeek(@Param("userCode") String userCode);
}