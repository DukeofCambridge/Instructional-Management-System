package com.tan.labbackend.dao;

import com.tan.labbackend.entity.Project;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface ProjectDAO extends JpaRepository<Project, Integer> {
    List<Project> findAllByCourseId(Integer courseId);

    List<Project> findByCourseIdAndEndTimeAfter(Integer courseId, Date time);
    List<Project> findByCourseIdAndEndTimeBefore(Integer courseId, Date time);

}
