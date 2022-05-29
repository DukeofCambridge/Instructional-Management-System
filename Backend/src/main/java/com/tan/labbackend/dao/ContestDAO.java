package com.tan.labbackend.dao;

import com.tan.labbackend.entity.Contest;
import com.tan.labbackend.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContestDAO extends JpaRepository<Contest, Integer> {
    void deleteById(int id);
    List<Contest> findAllByCourse(Course course);
}