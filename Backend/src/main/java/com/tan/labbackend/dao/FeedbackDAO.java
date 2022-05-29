package com.tan.labbackend.dao;

import com.tan.labbackend.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackDAO extends JpaRepository<Feedback, Integer> {
    List<Feedback> findAllByCourseId(Integer cid);
}
