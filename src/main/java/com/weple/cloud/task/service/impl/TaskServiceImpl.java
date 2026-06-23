package com.weple.cloud.task.service.impl;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.weple.cloud.file.FileInfoVO;
import com.weple.cloud.file.FileVO;
import com.weple.cloud.file.mapper.FileMapper;
import com.weple.cloud.task.mapper.TaskMapper;
import com.weple.cloud.task.service.TaskMemberVO;
import com.weple.cloud.task.service.TaskMilestoneVO;
import com.weple.cloud.task.service.TaskParentVO;
import com.weple.cloud.task.service.TaskPriorityVO;
import com.weple.cloud.task.service.TaskProjectSelectVO;
import com.weple.cloud.task.service.TaskService;
import com.weple.cloud.task.service.TaskStatusVO;
import com.weple.cloud.task.service.TaskTypeListVO;
import com.weple.cloud.task.service.TaskVO;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
	private final FileMapper fileMapper;
	private final TaskMapper taskMapper;
	
	@Value("${file.upload.task-dir}")
    private String uploadDir;
	
	//내부 일감 전체 조회
	@Override
	public List<TaskVO> findAll(Long pId) {
		return taskMapper.selectAll(pId);
	}
	//일감유형 목록 조회
	@Override
	public List<TaskTypeListVO> findType(Long cId) {
		return taskMapper.taskTypes(cId);
	}
	//일감 상태 목록 조회
	@Override
	public List<TaskStatusVO> findStatus() {
		return taskMapper.taskStatuses();
	}
	//프로젝트 참여자 목록 조회 담당자 지정
	@Override
	public List<TaskMemberVO> findMember(Long pId) {
		return taskMapper.taskMembers(pId);
	}
	//우선순위 목록 조회
	@Override
	public List<TaskPriorityVO> findPriority(Long cId) {
		return taskMapper.taskPriorities(cId);
	}
	//상위 일감 목록 조회
	@Override
	public List<TaskParentVO> findParent(Long pId) {
		return taskMapper.taskParents(pId);
	}
	// 마일스톤 목록 조회
	@Override
	public List<TaskMilestoneVO> findMilestone(Long pId) {
		return taskMapper.taskMilestones(pId);
	}
	// 등록 쿼리
	@Override
    @Transactional
    public int insertTask(TaskVO taskVO, List<MultipartFile> files) throws Exception {
        
        // 1. 일감 DB 등록
        int result = taskMapper.insertTask(taskVO);
        String currentTaskId = taskVO.getTaskId();

        // 2. 파일 체크
        if (files == null || files.isEmpty()) {
            return result;
        }

        // 💡 3. 주입받은 uploadDir 경로 사용
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs(); // 폴더 생성 (EC2에서는 이 폴더에 대한 쓰기 권한이 필요할 수 있습니다)
        }

        // 4. 파일 저장 및 DB INSERT 로직 (이전과 동일)
        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;

            String originalFileName = file.getOriginalFilename();
            String savedName = UUID.randomUUID().toString() + "_" + originalFileName;
            
            // 💡 경로 조합 시 안전하게 File.separator 사용 권장
            String filePath = uploadDir + savedName; 
            long fileSize = file.getSize();

            // 실제 파일 저장
            File dest = new File(filePath);
            file.transferTo(dest);

            // DB 파일 중복 조회
            Long fileId = fileMapper.findFileId(currentTaskId, originalFileName);

            if (fileId == null) {
                FileVO fileVO = new FileVO();
                fileVO.setTaskId(currentTaskId);
                fileVO.setFileName(originalFileName);
                fileMapper.insertFile(fileVO);
                fileId = fileVO.getFileId();
            }

            FileInfoVO fileInfoVO = new FileInfoVO();
            fileInfoVO.setFileId(fileId);
            fileInfoVO.setFilePath(filePath);
            fileInfoVO.setFileSize(fileSize);
            fileInfoVO.setUploader(taskVO.getUserCode()); 
            fileInfoVO.setSavedName(savedName);
            
            fileMapper.insertFileInfo(fileInfoVO);
        }
        return result;
    }

	// 상세조회
	@Override
	public TaskVO findTaskDetail(String tId) {
		return taskMapper.taskDetail(tId);
	}
	// 상세 조회 하위 일감
	@Override
	public List<TaskVO> findChildTask(String tId) {
		return taskMapper.childTask(tId);
	}
	
	// 전체 일감 조회
	@Override
	public List<TaskVO> findAllList(String tManager) {
		
		return taskMapper.selectAllList(tManager);
	}
	// 프로젝트 전체 본인의 모든 일감 조회
	@Override
	public List<TaskProjectSelectVO> findMyProject(String uCode) {
		return taskMapper.myAllTasks(uCode);
	}

	
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void updateTask(TaskVO taskVO, List<MultipartFile> files) throws Exception {
	    // 1. 일감 기본 정보 업데이트
	    int result = taskMapper.updateTask(taskVO);
	    
	    if (result > 0) {
	        // 2. 파일이 새로 업로드된 경우 파일 저장 및 DB Insert 로직 진행
	        if (files != null && !files.isEmpty()) {
	            for (MultipartFile file : files) {
	                if (!file.isEmpty()) {
	                    // [기존 insertTask와 동일한 파일 업로드 로직 구현]
	                    // FileVO fileVO = fileUtils.uploadFile(file);
	                    // fileVO.setTaskId(taskVO.getTaskId());
	                    // taskMapper.insertFile(fileVO);
	                }
	            }
	        }
	    }
	}
	// 삭제
	@Override
	public void deleteTask(String tId) {
		taskMapper.deleteTask(tId);
	}

}
