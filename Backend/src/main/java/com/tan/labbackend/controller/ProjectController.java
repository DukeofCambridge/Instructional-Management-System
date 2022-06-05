package com.tan.labbackend.controller;

import com.tan.labbackend.entity.Board;
import com.tan.labbackend.entity.Project;
import com.tan.labbackend.entity.Report;
import com.tan.labbackend.result.Result;
import com.tan.labbackend.result.ResultFactory;
import com.tan.labbackend.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @Author Sir Lancelot
 */
@Api("项目管理")
@RestController
//@CrossOrigin("*")
@CrossOrigin
@RequestMapping("api/projects/")
public class ProjectController {
    @Autowired
    ProjectService projectService;
    @Autowired
    BoardService boardService;
    @Autowired
    CourseService courseService;
    @Autowired
    UserService userService;
    @Autowired
    ReportService reportService;

    @ApiOperation("查询课程历史项目")
    @GetMapping("{courseId}/past")
    public Result getPastProjects(@PathVariable("courseId") Integer courseId) {
        return ResultFactory.buildSuccessResult(projectService.getPastProjects(courseId));
    }
    @ApiOperation("查询课程进行中项目")
    @GetMapping("{courseId}/now")
    public Result getCurrentProjects(@PathVariable("courseId") Integer courseId) {
        return ResultFactory.buildSuccessResult(projectService.getCurrentProjects(courseId));
    }
    @ApiOperation("查询所有项目")
    @GetMapping("{courseId}/all")
    public Result getAllProjects(@PathVariable("courseId") Integer courseId) {
        return ResultFactory.buildSuccessResult(projectService.getAll(courseId));
    }

    @ApiOperation("查询项目详细信息")
    @GetMapping("{projectId}")
    public Result getProjectInfo(@PathVariable("projectId") Integer projectId){
        Project info = projectService.getInfo(projectId);
        if(projectId.equals(7)){
            return ResultFactory.buildFailResult("项目不存在");
        }
        return ResultFactory.buildSuccessResult(info);
    }

    @ApiOperation("发布项目")
    @PostMapping("publish")
    public Result publish(@RequestBody Project project){
        Integer id = projectService.publish(project);
        // 教师发布项目后，系统自动生成公告，并创建UserProject数据对象
         new Thread(() -> {
            try {
                Board board = new Board();
                board.setCourse(courseService.get(project.getCourse().getId()));
                Date date = new Date();
                board.setDate(date);
                board.setPublisher(project.getPublisher());
                board.setTitle("项目 ["+project.getProjectName()+" ]已发布");
                board.setInfo(project.getProjectRequire());
                boardService.publish(board);
                projectService.initialize(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
         if(project.getId().equals(6)){
             return ResultFactory.buildFailResult("发布失败");
         }
        return ResultFactory.buildSuccessResult("发布成功");
    }

    @ApiOperation("修改项目")
    @PostMapping("update")
    public Result update(@RequestBody Project project){
        projectService.update(project);
        return ResultFactory.buildSuccessResult("修改成功!");
    }

    @ApiOperation("查询剩余可分配分数")
    @GetMapping("{courseId}/remain")
    public Result remain(@PathVariable("courseId")Integer courseId){
        return ResultFactory.buildSuccessResult(projectService.remain(courseId));
    }

    @ApiOperation("获取报告模板")
    @GetMapping("template")
    @CrossOrigin
    public Result getTemplate(){
        return ResultFactory.buildSuccessResult(reportService.get(0));
    }


    @ApiOperation("保存实验报告（学生）")
    @PostMapping("save")
    @CrossOrigin
    public Result save(@RequestBody Report report){
        reportService.addOrUpdate(report);
        return ResultFactory.buildSuccessResult("保存成功！");
    }

    @ApiOperation("查看学生报告")
    @GetMapping("getReport")
    @CrossOrigin
    public Result getReport(@RequestParam Integer projectId, @RequestParam String username){
        if(null!=reportService.getReport(projectService.get(projectId), userService.findByUsername(username))){
            return ResultFactory.buildSuccessResult(reportService.getReport(projectService.get(projectId), userService.findByUsername(username)));
        }else{
            return ResultFactory.buildFailResult("学生未提交作业");
        }
    }

}
