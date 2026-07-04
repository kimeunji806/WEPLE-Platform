package com.weple.cloud.dashboard.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.weple.cloud.dashboard.mapper.DashboardMapper;
import com.weple.cloud.dashboard.service.DashboardService;
import com.weple.cloud.task.service.TaskVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final DashboardMapper dashboardMapper;

    @Override
    public List<TaskVO> getTasksDueWithinAWeek(String userCode) {
        return dashboardMapper.selectTasksDueWithinAWeek(userCode);
    }
}