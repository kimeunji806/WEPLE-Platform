package com.weple.cloud.project.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import com.weple.cloud.project.service.ProjectVO;

@Mapper
public interface ProjectMapper {
	public List<ProjectVO> selectAll(
			 @Param("keyword") String keyword,
		     @Param("offset") int offset,
		     @Param("pageSize") int pageSize
		     );
	public List<ProjectVO> selectAllNoPage(@Param("keyword") String keyword);
	public List<String> selectModuleNames(@Param("projectId") Long projectId);
	
	int countAll(@Param("keyword") String keyword);
	
	public ProjectVO selectById(String projectId);
}
