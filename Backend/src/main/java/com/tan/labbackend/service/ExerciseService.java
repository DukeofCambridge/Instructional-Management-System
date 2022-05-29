package com.tan.labbackend.service;

import com.tan.labbackend.dao.ExerciseDAO;
import com.tan.labbackend.entity.Exercise;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class ExerciseService {

    @Autowired
    private ExerciseDAO exerciseDAO;

    /**
     * 获取相应课程所有问题
     */
    public List<Exercise> getAllQuestionByCourseId(Integer cid) {

        return exerciseDAO.findAllByCourseId(cid);

    }

    /**
     * 随机获取相应课程相应数量题目
     */
    public List<Exercise> getQuestionsByCourseId(Integer cid, Integer count) {
        Random random = new Random(2323);
        List<Exercise> exercises = exerciseDAO.findAllByCourseId(cid);
        Collections.shuffle(exercises,random);
        try{
            return exercises.subList(0,count);
        }
        catch (IllegalArgumentException e){
            log.error("课程{}题目数量不足{}",cid,count);
        }
        return exercises;
    }
}
