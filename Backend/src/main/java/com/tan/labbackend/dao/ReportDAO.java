package com.tan.labbackend.dao;

import com.tan.labbackend.entity.Project;
import com.tan.labbackend.entity.Report;
import com.tan.labbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportDAO extends JpaRepository<Report, Integer> {
    List<Report> findAllByProjectAndUser(Project project, User user);
}
