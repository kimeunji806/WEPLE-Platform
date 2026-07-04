package com.weple.cloud.dashboard.service;

import java.util.List;

import com.weple.cloud.task.service.TaskVO;

public interface DashboardService {

	List<TaskVO> getTasksDueWithinAWeek(String userCode);

}
