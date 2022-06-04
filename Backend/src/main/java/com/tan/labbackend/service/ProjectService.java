package com.tan.labbackend.service;

import com.tan.labbackend.dao.ReportDAO;
import com.tan.labbackend.entity.*;
import com.tan.labbackend.dao.ProjectDAO;
import com.tan.labbackend.dao.UserProjectDAO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Author Sir Lancelot
 */
@Service
@AllArgsConstructor
public class ProjectService {
    @Autowired
    ProjectDAO projectDAO;
    @Autowired
    UserCourseService userCourseService;
    @Autowired
    UserProjectDAO userProjectDAO;

    public Project get(int id){
        return projectDAO.findById(id).orElse(null);
    }
    // 查询课程历史项目(已过截止时间)
    public List<Project> getPastProjects(Integer courseId){
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(2023, Calendar.JANUARY,4,12,01,01);
//        Date date = calendar.getTime();
        return projectDAO.findByCourseIdAndEndTimeBefore(courseId, new Date());
    }
    // 查询进行中的项目
    public List<Project> getCurrentProjects(Integer courseId){
        Date date = new Date();
        return projectDAO.findByCourseIdAndEndTimeAfter(courseId, date);
    }
    public List<Project> getAll(Integer courseId){
        return projectDAO.findAllByCourseId(courseId);
    }

    public Project getInfo(Integer projectId){
        return projectDAO.findById(projectId).orElse(null);
    }

    // 查询已发布项目的总成绩比例
    public Double remain(Integer courseId){
        List<Project> projects = projectDAO.findAllByCourseId(courseId);
        Double re = 0.0;
        for(Project project : projects){
            re+=project.getProjectProportion();
        }
        return re;
    }
    public Integer publish(Project project){
        return projectDAO.save(project).getId();
    }

    // 教师发布一个项目后 应创建UserProject联系实体
    @Transactional
    public void initialize(Integer pid){
        Integer courseId = this.get(pid).getCourse().getId();
        List<User> sl = userCourseService.findAllStudents(courseId);
        UserProject up = new UserProject();
        up.setProjectId(pid);
        for(User u: sl){
            up.setStudentId(u.getId());
            userProjectDAO.save(up);
        }
    }
    @Transactional
    public void update(Project project){
        projectDAO.save(project);
    }

}
