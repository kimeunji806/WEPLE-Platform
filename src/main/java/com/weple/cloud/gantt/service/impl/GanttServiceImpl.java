package com.weple.cloud.gantt.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.weple.cloud.gantt.mapper.GanttMapper;
import com.weple.cloud.gantt.service.GanttResponseDTO;
import com.weple.cloud.gantt.service.GanttService;
import com.weple.cloud.gantt.service.GanttTaskElementVO;
import com.weple.cloud.milestone.mapper.MilestoneMapper;
import com.weple.cloud.milestone.service.MilestoneInfoVO;
import com.weple.cloud.task.mapper.TaskMapper;
import com.weple.cloud.task.service.TaskVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GanttServiceImpl implements GanttService {

    private final MilestoneMapper milestoneMapper;
    private final TaskMapper taskMapper;
    private final GanttMapper ganttMapper;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    private String getMilestoneStatusText(String status) {
        if ("g1".equals(status)) return "진행 중";
        if ("g2".equals(status)) return "완료";
        return status;
    }

    private String getTaskStatusText(String status) {
        if ("e1".equals(status)) return "신규";
        if ("e2".equals(status)) return "진행 중";
        if ("e3".equals(status)) return "완료";
        if ("e4".equals(status)) return "결함";
        return status != null ? status : "신규";
    }

    @Override
    public GanttResponseDTO getGanttChartData(Long projectId) {
        List<GanttTaskElementVO> ganttDataList = new ArrayList<>();

        // 1. [수정] 새로 만든 간트차트 전용 경량 쿼리 호출 (속도 극대화)
        List<MilestoneInfoVO> parentMilestones = ganttMapper.selectMilestoneForGantt(projectId);
        
        // 2. 해당 프로젝트의 전체 일감(Task) 가져오기
        List<TaskVO> allTasks = ganttMapper.selectTaskAll(projectId);
        
        // ==========================================================
        // 💡 [TaskProgress Long 타입 반영] 마일스톤 진척도 계산
        // ==========================================================
        // 마일스톤 ID별 일감들의 진척도(Long) 리스트를 수집하는 맵
        Map<Long, List<Long>> milestoneTaskProgressMap = new HashMap<>();
        for (TaskVO task : allTasks) {
            if (task.getMilestoneId() != null) {
                milestoneTaskProgressMap.computeIfAbsent(task.getMilestoneId(), k -> new ArrayList<>())
                                        .add(task.getTaskProgress() != null ? task.getTaskProgress() : 0L);
            }
        }

        for (MilestoneInfoVO parent : parentMilestones) {
            int totalChildProgress = 0;
            int childCount = 0;

            // A. 하위 마일스톤(자식)들의 진척도 계산
            if (parent.getChildMilestones() != null) {
                for (MilestoneInfoVO child : parent.getChildMilestones()) {
                    if (child.getMilestoneId() == null) continue;
                    
                    List<Long> childTasks = milestoneTaskProgressMap.get(child.getMilestoneId());
                    double childAvg = 0;
                    
                    if (childTasks != null && !childTasks.isEmpty()) {
                        // mapToLong()과 average()를 사용하여 Long 리스트의 평균(double) 계산
                        childAvg = childTasks.stream().mapToLong(Long::longValue).average().orElse(0.0);
                    }
                    
                    // MilestoneInfoVO의 progressPercentage(int)에 맞게 반올림 후 주입
                    child.setProgressPercentage((int) Math.round(childAvg));
                    
                    totalChildProgress += child.getProgressPercentage();
                    childCount++;
                }
            }

            // B. 상위 마일스톤(부모)의 진척도 계산 (자식 마일스톤들의 평균)
            if (childCount > 0) {
                parent.setProgressPercentage((int) Math.round((double) totalChildProgress / childCount));
            } else {
                // [방어 코드] 상위 마일스톤에 직속 일감이 있을 경우 처리
                List<Long> parentTasks = milestoneTaskProgressMap.get(parent.getMilestoneId());
                if (parentTasks != null && !parentTasks.isEmpty()) {
                    double parentAvg = parentTasks.stream().mapToLong(Long::longValue).average().orElse(0.0);
                    parent.setProgressPercentage((int) Math.round(parentAvg));
                } else {
                    parent.setProgressPercentage(0);
                }
            }
        }
        // ==========================================================

        // 3. ★ 각 하위 마일스톤별 일감의 "가장 빠른 시작일" 추출하기 (이하 기존 코드 동일)
        Map<Long, LocalDateTime> childMilestoneStartMap = new HashMap<>();
        for (TaskVO task : allTasks) {
            if (task.getMilestoneId() != null && task.getStartDate() != null) {
                childMilestoneStartMap.merge(
                    task.getMilestoneId(), 
                    task.getStartDate().atStartOfDay(), 
                    (oldDate, newDate) -> newDate.isBefore(oldDate) ? newDate : oldDate
                );
            }
        }

        // 4. ★ 하위 마일스톤 시작일을 기반으로 "상위 마일스톤의 가장 빠른 시작일" 추적
        Map<Long, LocalDateTime> parentMilestoneStartMap = new HashMap<>();
        for (MilestoneInfoVO parent : parentMilestones) {
            if (parent.getChildMilestones() != null) {
                for (MilestoneInfoVO child : parent.getChildMilestones()) {
                    LocalDateTime childStart = childMilestoneStartMap.get(child.getMilestoneId());
                    if (childStart != null) {
                        parentMilestoneStartMap.merge(
                            parent.getMilestoneId(),
                            childStart,
                            (oldDate, newDate) -> newDate.isBefore(oldDate) ? newDate : oldDate
                        );
                    }
                }
            }
        }
        
        // 5. 하위 일감의 마일스톤 역추적을 위한 사전 맵(Map) 생성
        Map<String, Long> taskMilestoneMap = new HashMap<>();
        for (TaskVO task : allTasks) {
            if (task.getMilestoneId() != null) {
                taskMilestoneMap.put(task.getTaskId(), task.getMilestoneId()); 
            }
        }

        // 6. 시작일 기준 정렬
        parentMilestones.sort((p1, p2) -> {
            LocalDateTime t1 = parentMilestoneStartMap.get(p1.getMilestoneId());
            LocalDateTime t2 = parentMilestoneStartMap.get(p2.getMilestoneId());
            if (t1 == null) t1 = (p1.getFinishDate() != null) ? p1.getFinishDate().atStartOfDay() : LocalDateTime.now();
            if (t2 == null) t2 = (p2.getFinishDate() != null) ? p2.getFinishDate().atStartOfDay() : LocalDateTime.now();
            return t1.compareTo(t2);
        });

        for (MilestoneInfoVO parent : parentMilestones) {
            if (parent.getChildMilestones() != null) {
                parent.getChildMilestones().sort((c1, c2) -> {
                    LocalDateTime t1 = childMilestoneStartMap.get(c1.getMilestoneId());
                    LocalDateTime t2 = childMilestoneStartMap.get(c2.getMilestoneId());
                    if (t1 == null) t1 = (c1.getFinishDate() != null) ? c1.getFinishDate().atStartOfDay() : LocalDateTime.now();
                    if (t2 == null) t2 = (c2.getFinishDate() != null) ? c2.getFinishDate().atStartOfDay() : LocalDateTime.now();
                    return t1.compareTo(t2);
                });
            }
        }

        allTasks.sort((t1, t2) -> {
            LocalDateTime d1 = t1.getStartDate() != null ? t1.getStartDate().atStartOfDay() : LocalDateTime.MAX;
            LocalDateTime d2 = t2.getStartDate() != null ? t2.getStartDate().atStartOfDay() : LocalDateTime.MAX;
            return d1.compareTo(d2);
        });

        // 7. 일감 구조화 및 부모별 그룹핑
        Map<String, List<GanttTaskElementVO>> childrenTasksMap = new HashMap<>();
        List<GanttTaskElementVO> rootTasks = new ArrayList<>();

        for (TaskVO task : allTasks) {
            GanttTaskElementVO taskElement = new GanttTaskElementVO();
            taskElement.setId("T_" + task.getTaskId());
            
            String taskStatusName = getTaskStatusText(task.getTaskStatus()); 
            taskElement.setText("[" + taskStatusName + "] " + task.getTaskTitle());
            
            if (task.getStartDate() != null) {
                taskElement.setStart_date(task.getStartDate().atStartOfDay().format(DATE_FORMATTER));
            }
            if (task.getStartDate() != null && task.getFinishDate() != null) {
                long days = ChronoUnit.DAYS.between(task.getStartDate(), task.getFinishDate()) + 1;
                taskElement.setDuration((int) days);
            } else {
                taskElement.setDuration(1);
            }
            taskElement.setProgress((task.getTaskProgress() != null ? task.getTaskProgress() : 0) / 100.0);
            taskElement.setType("task");

            Long targetMilestoneId = task.getMilestoneId();
            if (targetMilestoneId == null && task.getParentTaskId() != null && !task.getParentTaskId().isEmpty()) {
                targetMilestoneId = taskMilestoneMap.get(task.getParentTaskId());
            }

            String parentId = null;
            if (targetMilestoneId != null) {
                parentId = "M_" + targetMilestoneId;
            }
            taskElement.setParent(parentId);

            if (parentId == null) {
                rootTasks.add(taskElement);
            } else {
                childrenTasksMap.computeIfAbsent(parentId, k -> new ArrayList<>()).add(taskElement);
            }
        }

        // 8. 계층 역순 추적 트리 조립 (Pre-order 순서 배치)
        for (MilestoneInfoVO parent : parentMilestones) {
            GanttTaskElementVO parentElement = new GanttTaskElementVO();
            parentElement.setId("M_" + parent.getMilestoneId());
            
            String parentStatusName = getMilestoneStatusText(parent.getMilestoneStatus());
            parentElement.setText("[" + parentStatusName + "] " + parent.getMilestoneTitle());
            
            LocalDateTime actualParentEnd = (parent.getFinishDate() != null) ? parent.getFinishDate().atStartOfDay() : LocalDateTime.now();
            LocalDateTime parentStart = parentMilestoneStartMap.get(parent.getMilestoneId());
            LocalDateTime actualParentStart = (parentStart != null) ? parentStart : actualParentEnd;
            
            if (actualParentStart.isAfter(actualParentEnd)) {
                actualParentStart = actualParentEnd; 
            }
            
            parentElement.setStart_date(actualParentStart.format(DATE_FORMATTER));
            long parentDuration = ChronoUnit.DAYS.between(actualParentStart, actualParentEnd) + 1;
            parentElement.setDuration((int) parentDuration); 
            
            parentElement.setProgress(parent.getProgressPercentage() / 100.0);
            parentElement.setParent(null); 
            parentElement.setType("task"); 
            ganttDataList.add(parentElement);
            
            appendTasksRecursively("M_" + parent.getMilestoneId(), childrenTasksMap, ganttDataList);

            if (parent.getChildMilestones() != null) {
                for (MilestoneInfoVO child : parent.getChildMilestones()) {
                    if (child.getMilestoneId() == null) continue;
                    
                    GanttTaskElementVO childElement = new GanttTaskElementVO();
                    childElement.setId("M_" + child.getMilestoneId());
                    
                    String childStatusName = getMilestoneStatusText(child.getMilestoneStatus());
                    childElement.setText("[" + childStatusName + "] " + child.getMilestoneTitle());
                    
                    LocalDateTime actualChildEnd = (child.getFinishDate() != null) ? child.getFinishDate().atStartOfDay() : LocalDateTime.now();
                    LocalDateTime childStart = childMilestoneStartMap.get(child.getMilestoneId());
                    LocalDateTime actualChildStart = (childStart != null) ? childStart : actualChildEnd;
                    
                    if (actualChildStart.isAfter(actualChildEnd)) {
                        actualChildStart = actualChildEnd;
                    }
                    
                    childElement.setStart_date(actualChildStart.format(DATE_FORMATTER));
                    long childDuration = ChronoUnit.DAYS.between(actualChildStart, actualChildEnd) + 1;
                    childElement.setDuration((int) childDuration);
                    
                    childElement.setProgress(child.getProgressPercentage() / 100.0);
                    childElement.setParent("M_" + parent.getMilestoneId()); 
                    childElement.setType("task"); 
                    ganttDataList.add(childElement);
                    
                    appendTasksRecursively("M_" + child.getMilestoneId(), childrenTasksMap, ganttDataList);
                }
            }
        }

        for (GanttTaskElementVO rootTask : rootTasks) {
            ganttDataList.add(rootTask);
            appendTasksRecursively(rootTask.getId(), childrenTasksMap, ganttDataList);
        }

        GanttResponseDTO response = new GanttResponseDTO();
        response.setData(ganttDataList);
        return response;
    }
    
    /**
     * 💡 [추가] 특정 부모 요소(마일스톤 또는 일감)에 소속된 일감들을 
     * 계단식 순서대로 재귀 호출하며 평탄화 리스트에 끼워 넣는 헬퍼 메서드
     */
    private void appendTasksRecursively(String parentId, Map<String, List<GanttTaskElementVO>> childrenTasksMap, List<GanttTaskElementVO> ganttDataList) {
        List<GanttTaskElementVO> tasks = childrenTasksMap.get(parentId);
        if (tasks != null) {
            for (GanttTaskElementVO task : tasks) {
                ganttDataList.add(task);
                // 만약 이 일감이 또 다른 서브 일감(Sub-task)을 품고 있다면 재귀 호출하여 바로 아래에 계단식으로 장착
                appendTasksRecursively(task.getId(), childrenTasksMap, ganttDataList);
            }
        }
    }
}
