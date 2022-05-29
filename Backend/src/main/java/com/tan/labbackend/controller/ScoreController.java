package com.tan.labbackend.controller;

import com.tan.labbackend.dao.UserDAO;
import com.tan.labbackend.dao.UserProjectDAO;
import com.tan.labbackend.entity.Board;
import com.tan.labbackend.entity.UserProject;
import com.tan.labbackend.result.Result;
import com.tan.labbackend.result.ResultFactory;
import com.tan.labbackend.service.ScoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Api("成绩管理")
//@CrossOrigin("*")
@RestController
@RequestMapping("api/score/")
public class ScoreController {
    @Autowired
    ScoreService scoreService;
    @Autowired
    UserProjectDAO userProjectDAO;
    @Autowired
    UserDAO userDAO;

    @ApiOperation("所有学生成绩")
    @CrossOrigin
    @GetMapping("{courseId}")
    public Result getAll(@PathVariable("courseId") Integer courseId){
        return ResultFactory.buildSuccessResult(scoreService.getAll(courseId));
    }
    @ApiOperation("批成绩")
    @CrossOrigin
    @PostMapping("mark")
    public Result mark(@RequestParam String username, @RequestParam Integer projectId, @RequestParam String score){
        UserProject userProject = userProjectDAO.findByProjectIdAndStudentId(projectId, userDAO.findByUsername(username).getId());
        userProject.setProjectGrade(Double.parseDouble(score));
        scoreService.mark(userProject);
//        new Thread(() -> {
//            try {
//        scoreService.calcOverall(userProject.getStudentId(), userProject.getProjectId());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();
        return ResultFactory.buildSuccessResult("成功");
    }
    @ApiOperation("查看某学生某项目的成绩")
    @GetMapping("get")
    public Result get(@RequestParam Integer uid, @RequestParam Integer pid){
        if(null!=userProjectDAO.findByProjectIdAndStudentId(pid, uid).getProjectGrade()){
            return ResultFactory.buildSuccessResult(userProjectDAO.findByProjectIdAndStudentId(pid, uid).getProjectGrade());
        }else {
            return ResultFactory.buildSuccessResult("NA");
        }
    }
    @ApiOperation("查看课程成绩统计信息")
    @GetMapping("{courseId}/statistics")
    public Result statistics(@PathVariable("courseId") Integer courseId){
        return ResultFactory.buildSuccessResult(scoreService.ratio(courseId));
    }
}
