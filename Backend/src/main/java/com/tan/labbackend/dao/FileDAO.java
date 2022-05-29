package com.tan.labbackend.dao;

import com.tan.labbackend.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileDAO extends JpaRepository<File, Integer> {
    List<File> findAllByprojectId(Integer projectId);
//    File findById(Integer file_id);
//    User findByEmail(String email);
    File findFileById(Integer file_id);
}
