package com.tan.labbackend.service;

import com.tan.labbackend.entity.Contest;
import com.tan.labbackend.entity.Course;
import com.tan.labbackend.dao.ContestDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ContestService {
    @Autowired
    ContestDAO contestDAO;
    @Autowired
    CourseService courseService;
    public Contest get(int id){
        Contest c = contestDAO.findById(id).orElse(null);
        return c;
    }
    public List<Contest> findAllByCourseId(Integer cid){
        Course course = courseService.get(cid);
        if (course!=null) {
            return contestDAO.findAllByCourse(course);
        }
        return null;
    }

    @Transactional
    public int addOrUpdate(Contest contest){
        if(contest.getId()!=null&&this.get(contest.getId())!=null){
            return 1;
        }
        contestDAO.save(contest);
        return 0;
    }

    @Transactional
    public void deleteByContestId(Integer cid){
        contestDAO.deleteById(cid);
    }
}
