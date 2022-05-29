package com.tan.labbackend.dao;

import com.tan.labbackend.entity.UserCourse;
import com.tan.labbackend.entity.UserCourseId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCourseDAO extends JpaRepository<UserCourse, UserCourseId> {
    List<UserCourse> findAllByCourseId(Integer cid);
    List<UserCourse> findAllByUserId(Integer uid);

    void deleteByUserIdAndCourseId(Integer uid,Integer cid);

    UserCourse findByCourseIdAndUserId(Integer courseId, Integer uid);
}
