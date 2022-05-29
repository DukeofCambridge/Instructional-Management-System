package com.tan.labbackend.dao;

import com.tan.labbackend.entity.UserProject;
import com.tan.labbackend.entity.UserProjectId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserProjectDAO extends JpaRepository<UserProject, UserProjectId> {
    List<UserProject> findAllByStudentId(Integer id);
    UserProject findByProjectIdAndStudentId(Integer pid, Integer sid);
}
