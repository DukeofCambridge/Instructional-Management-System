package com.tan.labbackend.service;

import com.tan.labbackend.dao.ProjectDAO;
import com.tan.labbackend.entity.*;
import com.tan.labbackend.dao.UserCourseDAO;
import com.tan.labbackend.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserCourseService {
    @Autowired
    UserCourseDAO userCourseDAO;
    @Autowired
    UserService userService;
    @Autowired
    UserDAO userDAO;

    public List<UserCourse> findAllbyUserId(Integer uid){
        return userCourseDAO.findAllByUserId(uid);
    }
    public List<User> findAllMemberbyCourseId(Integer cid){
        List<Integer> uids = userCourseDAO.findAllByCourseId(cid)
                .stream().map(UserCourse::getUserId).collect(Collectors.toList());
        List<User> users = uids.stream().map(uid->userService.get(uid)).collect(Collectors.toList());
        return users;
    }
    public Double getOverall(Integer courseId, Integer studentId){
        return userCourseDAO.findByCourseIdAndUserId(courseId, studentId).getProjectGrade();
    }
    /**
     * 批量导入课程成员，支持教师和学生一起导入
     */
    @Transactional
    public void addMembers(Integer courseId, String[] usernameList){
//        userRoleDAO.deleteAllByUserID(userID);
        List<UserCourse> urs=new ArrayList<>();
        for(String un : usernameList) {
            UserCourse userCourse = new UserCourse();
            Integer uid = userDAO.findByUsername(un).getId();
            // 不重复添加
            if(userCourseDAO.findByCourseIdAndUserId(courseId, uid)!=null){
                continue;
            }
            userCourse.setUserId(uid);
            userCourse.setCourseId(courseId);
            urs.add(userCourse);
        }
        userCourseDAO.saveAll(urs);
    }

    @Transactional
    public void removeMembers(Integer courseId, String[] usernames){
        for(String un: usernames){
            Integer uid = userDAO.findByUsername(un).getId();
            userCourseDAO.deleteByUserIdAndCourseId(uid, courseId);
        }
    }

    /**
     * 找到课程所有学生
     */
    public List<User> findAllStudents(Integer id){
        List<User> result = new ArrayList<>();
        List<UserCourse> uc = userCourseDAO.findAllByCourseId(id);
        for(UserCourse item: uc){
            User current = userDAO.getById(item.getUserId());
            if(current.getRole().getId()==5){
                result.add(current);
            }
        }
        return result;
    }


}
