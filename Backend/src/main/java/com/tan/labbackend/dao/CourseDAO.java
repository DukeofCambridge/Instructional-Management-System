package com.tan.labbackend.dao;

import com.tan.labbackend.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseDAO extends JpaRepository<Course, Integer> {
    void deleteById(int id);
    List<Course> findAllById(int cid);
    List<Course> findAllByIdIn(List<Integer> cids);
    Course findByCourseName(String name);
    List<Course> findAllByCourseNameContaining(String keyword);
}
