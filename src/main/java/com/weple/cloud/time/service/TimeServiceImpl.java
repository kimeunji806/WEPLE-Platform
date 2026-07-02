package com.weple.cloud.time.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.weple.cloud.task.mapper.TaskMapper;
import com.weple.cloud.time.mapper.TimeMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TimeServiceImpl implements TimeService {

	private final TimeMapper timeMapper;
	private final TaskMapper taskMapper;

	// -------------------------------프로젝트 내 소요시간------------------------------
	// 전체조회
	@Override
	public List<WorkTimeVO> findProjectTimeAll(Long projectId) {
		return timeMapper.projectTimeAll(projectId);
	}

	// 단건 조회
	@Override
	public WorkTimeVO findProjectTimeOne(long workId) {
		return timeMapper.projectTimeOne(workId);
	}

	// 등록
	@Transactional
	@Override
	public long addProjectTime(WorkTimeVO workTimeVO) {
		long result = timeMapper.insertProjectTime(workTimeVO);
		if (result > 0) {
			// 사용자가 입력한 진척도가 있으면(잠금 상태가 아니었으면) 먼저 이 일감에 직접 반영
			if (workTimeVO.getProgress() != null) {
				taskMapper.updateTaskProgress(workTimeVO.getTaskId(), workTimeVO.getProgress());
			}
			// 이 일감(하위일감)이 100%로 "완료"된 경우에만 상위 일감 진척도를 재계산해서 전파.
			// 100% 미만이면 상위 일감의 진척도는 하위일감이 생기기 전 값 그대로 유지되어야 하므로 호출하지 않음.
			if (workTimeVO.getProgress() != null && workTimeVO.getProgress() == 100L) {
				taskMapper.updateHierarchicalProgress(workTimeVO.getTaskId());
			}
	    }
		return result == 1 ? workTimeVO.getWorkId() : -1;
	}

	// 수정
	@Override
	public Map<String, Object> modifyProjectTime(WorkTimeVO workTimeVO) {
		Map<String, Object> map = new java.util.HashMap<>();
		long result = timeMapper.updateProjectTime(workTimeVO);
		// 프로시저 실행
		if (result == 1) {
			timeMapper.updateTaskSpentHours();
		}
		map.put("result", result);
		return map;
	}

	// 삭제
	@Override
	public long removeProjectTime(long workId) {
		long result = timeMapper.deleteProjectTime(workId);
		//프로시저 실행
		if (result == 1) {
			timeMapper.updateTaskSpentHours();
		}
		return result;
	}

	// 일감별 소요시간 누적 합계 (프로시저)
	@Override
	public void updateTaskSpentHours() {
		timeMapper.updateTaskSpentHours();		
	}

}