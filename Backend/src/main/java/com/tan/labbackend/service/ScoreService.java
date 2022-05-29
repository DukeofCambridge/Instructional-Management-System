package com.tan.labbackend.service;

import com.tan.labbackend.dao.UserCourseDAO;
import com.tan.labbackend.dao.UserDAO;
import com.tan.labbackend.entity.Project;
import com.tan.labbackend.entity.User;
import com.tan.labbackend.entity.UserCourse;
import com.tan.labbackend.entity.UserProject;
import com.tan.labbackend.dao.ProjectDAO;
import com.tan.labbackend.dao.UserProjectDAO;
import com.tan.labbackend.dto.Score;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Sir Lancelot
 */
@Service
public class ScoreService {
    @Autowired
    UserProjectDAO userProjectDAO;
    @Autowired
    UserCourseService userCourseService;
    @Autowired
    ProjectDAO projectDAO;
    @Autowired
    UserCourseDAO userCourseDAO;
    @Autowired
    ProjectService projectService;
    @Autowired
    UserDAO userDAO;

    public List<Score> getAll(Integer courseId){
        List<Score> result = new ArrayList<>();
        List<User> sl = userCourseService.findAllStudents(courseId);
        List<Project> projects = projectDAO.findAllByCourseId(courseId);

        for(User u : sl){
            Score score = new Score();
            score.setUsername(u.getUsername());
            score.setName(u.getName());
            List<Double> grades = new ArrayList<>();
            for(Project p: projects){
                if(null!=userProjectDAO.findByProjectIdAndStudentId(p.getId(), u.getId()).getProjectGrade()){
                    grades.add(userProjectDAO.findByProjectIdAndStudentId(p.getId(), u.getId()).getProjectGrade());
                }else{
                    grades.add(-1.0);
                }
            }
            score.setGrades(grades);
            score.setOverall(userCourseService.getOverall(courseId, u.getId()));
            result.add(score);
        }
        return result;
    }

    @Transactional
    public void mark(@Valid @NotNull UserProject userProject){
        userProjectDAO.save(userProject);
        calcOverall(userProject.getStudentId(),userProject.getProjectId());
    }

    /**
     * 计算总成绩
     */
    @Transactional
    public void calcOverall(Integer uid, Integer pid){

        Integer cid = projectService.get(pid).getCourse().getId();

        List<Project> projects = projectDAO.findAllByCourseId(cid);
        double og = 0.0;
        for(Project p: projects){
            if(null!=userProjectDAO.findByProjectIdAndStudentId(p.getId(), uid).getProjectGrade()){
                og+=userProjectDAO.findByProjectIdAndStudentId(p.getId(), uid).getProjectGrade()*p.getProjectProportion()/100;
            }
        }
        UserCourse uc = userCourseDAO.findByCourseIdAndUserId(cid, uid);
        uc.setProjectGrade(og);
        userCourseDAO.save(uc);
    }
    /**
     * 计算总成绩比例
     */
    public List<Double> ratio(Integer courseId){
        List<UserCourse> student = new ArrayList<>();
        List<UserCourse> uc = userCourseDAO.findAllByCourseId(courseId);
        for(UserCourse item: uc){
            User current = userDAO.getById(item.getUserId());
            if(current.getRole().getId()==5){
                student.add(item);
            }
        }
        List<Double> re = new ArrayList<>();
        Double r1 =0.0,r2=0.0,r3=0.0,r4=0.0,r5=0.0;
        for(UserCourse u : student){
            Double o = u.getProjectGrade();
            if(o<60){
                r1+=1;
            }else if(o>=60&&o<70){
                r2+=1;
            }else if(o>=70&&o<80){
                r3+=1;
            }else if(o>=80&&o<90){
                r4+=1;
            }else if(o>=90){
                r5+=1;
            }
        }
        r1/=student.size();r2/=student.size();r3/=student.size();r4/=student.size();r5/=student.size();
        r1*=100;r2*=100;r3*=100;r4*=100;r5*=100;
        re.add(r1);re.add(r2);re.add(r3);re.add(r4);re.add(r5);
        return re;
    }
}
