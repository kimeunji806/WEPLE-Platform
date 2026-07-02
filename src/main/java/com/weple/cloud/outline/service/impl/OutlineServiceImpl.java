package com.weple.cloud.outline.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.weple.cloud.milestone.service.TaskGroupStatVO;
import com.weple.cloud.outline.mapper.OutlineMapper;
import com.weple.cloud.outline.service.OutlineService;
import com.weple.cloud.outline.service.ProjectGroupMemberDTO;
import com.weple.cloud.outline.service.ProjectProgressDTO;
import com.weple.cloud.outline.service.RawTaskDTO;
import com.weple.cloud.project.service.ProjectVO;

import lombok.RequiredArgsConstructor;

@Service // 스프링 빈 등록 필수
@RequiredArgsConstructor // 생성자 주입
public class OutlineServiceImpl implements OutlineService {

	private final OutlineMapper outlineMapper;
	
	@Override
	public ProjectVO getProjectById(Long projectId) {
	    return outlineMapper.selectProjectById(projectId);
	}
	
	@Override
	public List<ProjectGroupMemberDTO> selectProjectMembersByGroup(Long projectId) {
		return outlineMapper.selectProjectMembersByGroup(projectId);
	}
	
	
	
	
	@Override
	public ProjectProgressDTO getProjectProgress(Long projectId) {
	    ProjectProgressDTO progressDto = new ProjectProgressDTO();
	    
	    // 1. 단 한 번의 DB 조회로 모든 일감 로우 데이터 확보
	    List<RawTaskDTO> rawTasks = outlineMapper.selectRawTaskDetails(projectId);
	    if (rawTasks == null || rawTasks.isEmpty()) {
	        return progressDto; // 빈 데이터 객체 리턴
	    }

	    // 2. 종합 시간 및 진척도 계산
	    double totalEstimated = 0;
	    double totalSpent = 0;
	    int closedCount = 0;
	    int totalCount = rawTasks.size();

	    // 4대 기준 그룹핑을 위한 맵 선언 (Key: 그룹명, Value: [전체수, 완료수])
	    Map<String, int[]> statusMap = new HashMap<>();
	    Map<String, int[]> priorityMap = new HashMap<>();
	    Map<String, int[]> typeMap = new LinkedHashMap<>(); // 순서 유지를 위해 LinkedHashMap
	    Map<String, int[]> managerMap = new HashMap<>();

	    for (RawTaskDTO task : rawTasks) {
	        totalEstimated += task.getEstimatedTime();
	        totalSpent += task.getSpentHoursSum();
	        
	        boolean isClosed = "e3".equals(task.getTaskStatus());
	        if (isClosed) closedCount++;

	        // A. 상태별 매핑 (e1, e2, e3, e4 치환)
	        String statusName = "미지정";
	        if ("e1".equals(task.getTaskStatus())) statusName = "신규";
	        else if ("e2".equals(task.getTaskStatus())) statusName = "진행 중";
	        else if ("e3".equals(task.getTaskStatus())) statusName = "완료";
	        else if ("e4".equals(task.getTaskStatus())) statusName = "결함";
	        
	        int[] sArr = statusMap.computeIfAbsent(statusName, k -> new int[2]);
	        sArr[0]++; if (isClosed) sArr[1]++;

	        // B. 우선순위별 매핑 (NOT NULL 조건 반영)
	        if (task.getPriority() != null) {
	            int[] pArr = priorityMap.computeIfAbsent(task.getPriority(), k -> new int[2]);
	            pArr[0]++; if (isClosed) pArr[1]++;
	        }

	        // C. 유형별 매핑
	        if (task.getTypeName() != null) {
	            int[] tArr = typeMap.computeIfAbsent(task.getTypeName(), k -> new int[2]);
	            tArr[0]++; if (isClosed) tArr[1]++;
	        }

	        // D. 담당자별 매핑
	        int[] mArr = managerMap.computeIfAbsent(task.getManagerName(), k -> new int[2]);
	        mArr[0]++; if (isClosed) mArr[1]++;
	    }

	    // 종합 정보 세팅
	    progressDto.setTotalEstimatedHours(totalEstimated);
	    progressDto.setTotalSpentHours(totalSpent);
	    progressDto.setProgressPercentage(totalCount > 0 ? Math.round(((float) closedCount / totalCount) * 100) : 0);
	    progressDto.setTotalTaskCount(totalCount);

	    // 3. 맵 데이터를 TaskGroupStatVO 리스트로 변환 및 백분율 세팅
	    progressDto.setStatusStats(convertToStatList(statusMap));
	    progressDto.setPriorityStats(convertToStatList(priorityMap));
	    progressDto.setTypeStats(convertToStatList(typeMap));
	    progressDto.setManagerStats(convertToStatList(managerMap));

	    return progressDto;
	}

	// 자바 내부 변환 편의 메서드
	private List<TaskGroupStatVO> convertToStatList(Map<String, int[]> map) {
	    List<TaskGroupStatVO> list = new ArrayList<>();
	    map.forEach((groupName, counts) -> {
	        TaskGroupStatVO vo = new TaskGroupStatVO();
	        vo.setGroupName(groupName);
	        vo.setTotalCount(counts[0]);
	        vo.setClosedCount(counts[1]);
	        
	        int percentage = counts[0] > 0 ? Math.round(((float) counts[1] / counts[0]) * 100) : 0;
	        vo.setProgressPercentage(percentage);
	        
	        list.add(vo);
	    });
	    return list;
	}

}
