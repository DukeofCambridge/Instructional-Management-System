package com.tan.labbackend.dao;

import com.tan.labbackend.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExerciseDAO extends JpaRepository<Exercise, Integer> {
    List<Exercise> findAllByCourseId(Integer cid);
}