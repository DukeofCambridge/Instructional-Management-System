package com.tan.labbackend.dao;

import com.tan.labbackend.entity.Board;
import com.tan.labbackend.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardDAO extends JpaRepository<Board, Integer> {
    List<Board> findAllByCourse(Course course);
}
