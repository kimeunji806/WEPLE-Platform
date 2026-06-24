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
import com.weple.cloud.task.service.TaskCommentVO;
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
	
	// 등록
	@Override
    @Transactional
    public int insertTask(TaskVO taskVO, List<MultipartFile> files) throws Exception {
        
        // 일감 DB 등록
        int result = taskMapper.insertTask(taskVO);
        String currentTaskId = taskVO.getTaskId();

        // 파일 체크
        if (files == null || files.isEmpty()) {
            return result;
        }
        
        // 경로 properties에 저장해뒀음 배포때 aws 경로로 바꿔야됨
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs(); // aws에서의 권한 필요
        }

        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;

            String originalFileName = file.getOriginalFilename();
            String savedName = UUID.randomUUID().toString() + "_" + originalFileName;
            
            String filePath = uploadDir + savedName; 
            long fileSize = file.getSize();

            File dest = new File(filePath);
            file.transferTo(dest);

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

	// 일감 수정
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void updateTask(TaskVO taskVO, List<MultipartFile> files, List<Long> deletedFileIds) throws Exception {
	    
	    // [추가] 기존 첨부파일 삭제 처리 (물리 파일 삭제 및 DB 상태 변경)
	    if (deletedFileIds != null && !deletedFileIds.isEmpty()) {
	        for (Long fileId : deletedFileIds) {
	            // 1. 물리적 파일 삭제를 위해 해당 file_id의 모든 버전 정보(경로) 조회
	            List<FileInfoVO> fileVersions = fileMapper.findFileInfoByFileId(fileId);
	            
	            if (fileVersions != null) {
	                for (FileInfoVO fileInfo : fileVersions) {
	                    if (fileInfo.getFilePath() != null) {
	                        File physicalFile = new File(fileInfo.getFilePath());
	                        if (physicalFile.exists()) {
	                            physicalFile.delete(); // 폴더 내의 파일 삭제
	                        }
	                    }
	                }
	            }
	            
	            // 2. DB 업데이트: files 테이블의 is_deleted = 'Y'
	            fileMapper.updateFileDeletedStatus(fileId);
	            
	            // 3. DB 업데이트: file_info(file_version) 테이블의 경로 및 크기 정보 NULL 처리
	            fileMapper.clearFileVersionInfo(fileId);
	        }
	    }

	    // 기존 일감 정보 업데이트
	    taskMapper.updateTask(taskVO);
	    
	    // 추가된 파일이 존재할 경우 업로드 및 버전 관리 진행 (기존 코드 유지)
	    if (files != null && !files.isEmpty()) {
	        File dir = new File(uploadDir);
	        if (!dir.exists()) {
	            dir.mkdirs();
	        }

	        for (MultipartFile file : files) {
	            if (!file.isEmpty()) {
	                String originalFilename = file.getOriginalFilename(); 
	                
	                // 이제 is_deleted가 Y인 파일도 ID를 정상적으로 찾아옵니다.
	                Long existingFileId = fileMapper.findFileId(taskVO.getTaskId(), originalFilename);
	                
	                Long targetFileId;
	                
	                if (existingFileId != null) {
	                    // 기존 파일이 있으면 ID를 유지
	                    targetFileId = existingFileId;
	                    
	                    // [핵심 추가] 혹시 is_deleted가 'Y' 상태일 수 있으므로 'N'으로 업데이트
	                    fileMapper.restoreFile(targetFileId);
	                    
	                } else {
	                    // 아예 처음 올리는 새 파일이면 마스터 등록
	                    FileVO fileVO = new FileVO();
	                    fileVO.setTaskId(taskVO.getTaskId());
	                    fileVO.setFileName(originalFilename); 
	                    
	                    fileMapper.insertFile(fileVO);
	                    targetFileId = fileVO.getFileId(); 
	                }
	                
	                // 물리 파일 저장
	                String savedName = UUID.randomUUID().toString() + "_" + originalFilename;
	                String filePath = uploadDir + savedName; 
	                file.transferTo(new File(filePath));
	                
	                // 파일 버전(상세) 정보 등록
	                FileInfoVO fileInfoVO = new FileInfoVO();
	                fileInfoVO.setFileId(targetFileId);
	                fileInfoVO.setFilePath(filePath);
	                fileInfoVO.setFileSize(file.getSize());
	                fileInfoVO.setUploader(taskVO.getUserCode()); 
	                fileInfoVO.setSavedName(savedName);
	                
	                // 여기서 (기존 MAX 버전 + 1) 로직이 타면서 자연스럽게 다음 버전으로 Insert 됩니다.
	                fileMapper.insertFileInfo(fileInfoVO); 
	            }
	        }
	    }
	}
	// 삭제
	@Override
	public void deleteTask(String tId) {
		taskMapper.deleteTask(tId);
	}
	@Override
	public List<TaskCommentVO>findTaskComment(String tId) {
		return taskMapper.taskCommentList(tId);
	}

}
