package com.tan.labbackend.service;

import com.tan.labbackend.dao.FeedbackDAO;
import com.tan.labbackend.entity.*;
import com.tan.labbackend.dao.CourseDAO;
import com.tan.labbackend.dao.UserCourseDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {
    @Autowired
    CourseDAO courseDAO;
    @Autowired
    UserService userService;
    @Autowired
    UserCourseDAO userCourseDAO;
    @Autowired
    FeedbackDAO feedbackDAO;
    @Autowired
    UserCourseService userCourseService;
    public List<Course> findAllbyUsername(String username){
        int uid = userService.findByUsername(username).getId();
        List<Integer> cids = userCourseDAO.findAllByUserId(uid)
                .stream().map(UserCourse::getCourseId).collect(Collectors.toList());
        return courseDAO.findAllByIdIn(cids);
    }
    public List<Course> findAll(){

        return courseDAO.findAll();
    }
    public Course get(int id){
        return courseDAO.findById(id).orElse(null);
    }
//    public User getChargeTeacher(int courseId){
//        UserRole userRole =
//    }

    @Transactional
    public int addOrUpdate(Course course){
        if(course.getId()!=null&&this.get(course.getId())!=null){
            return 1;
        }
        if(courseDAO.findByCourseName(course.getCourseName())!=null){
            return 2;
        }
        courseDAO.save(course);
        return 0;
    }

    @Transactional
    public void deleteByCourseId(Integer cid){
        courseDAO.deleteById(cid);
    }
    public List<Course> findlike(String keyword){
        return courseDAO.findAllByCourseNameContaining(keyword);
    }

    @Transactional
    public  void setmain(Integer cid, String username){
        Course course = get(cid);
        course.setTeacherName(userService.findByUsername(username).getName());
        courseDAO.save(course);
    }
    public List<Feedback> getAllF(Integer cid){
        return feedbackDAO.findAllByCourseId(cid);
    }
    @Transactional
    public void feedback(Feedback feedback){
        Date date = new Date();
        feedback.setTime(date);
        feedbackDAO.save(feedback);
    }
}
