package com.tan.labbackend.controller;

import com.tan.labbackend.entity.*;
import com.tan.labbackend.dto.UserDTO;
import com.tan.labbackend.result.Result;
import com.tan.labbackend.result.ResultFactory;
import com.tan.labbackend.service.BoardService;
import com.tan.labbackend.service.CourseService;
import com.tan.labbackend.service.UserCourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.NonNull;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Api("课程管理")
@RestController
//@CrossOrigin("*")
@CrossOrigin
@RequestMapping("api/courses/")
public class CourseController {
    @Autowired
    CourseService courseService;
    @Autowired
    UserCourseService userCourseService;
    @Autowired
    BoardService boardService;

    @ApiOperation("查看全校课程表")
    @GetMapping("all")
    @CrossOrigin
    public Result findAllCourse() {
        return ResultFactory.buildSuccessResult(courseService.findAll());
    }

    @ApiOperation("查看自己课程表")
    @GetMapping("mine/{username}")
    @CrossOrigin
    public Result findSubjectCourse(@PathVariable("username") String username) {
        return ResultFactory.buildSuccessResult(courseService.findAllbyUsername(username));
    }

    @ApiOperation("获取课程人员信息")
    @GetMapping("{courseId}/members")
    public Result findCourseMember(@PathVariable("courseId") Integer courseId) {
        List<User> users = userCourseService.findAllMemberbyCourseId(courseId);
        List<UserDTO> result =  users
                .stream().map(user -> (UserDTO) new UserDTO().convertFor(user)).collect(Collectors.toList());

        return ResultFactory.buildSuccessResult(result);
    }

    @ApiOperation("获取课程介绍信息")
    @GetMapping("{courseId}/description")
    public Result findDescriptionbyCourseId(@PathVariable("courseId") Integer courseId) {
        return ResultFactory.buildSuccessResult(courseService.get(courseId));
    }

    @ApiOperation("管理员或教师修改课程")
    @CrossOrigin
    @PostMapping("addcourse")
    public  Result updateCourse(@RequestBody @Valid Course course){
        courseService.addOrUpdate(course);
        return ResultFactory.buildSuccessResult("更新课程信息成功");
    }

    @ApiOperation("管理员或教师删除课程")
    @CrossOrigin
    @PostMapping("deletecourse")
    public  Result deleteCourse(@RequestParam @Valid @NonNull int courseId){
        courseService.deleteByCourseId(courseId);
        return ResultFactory.buildSuccessResult("删除课程成功");
    }

    @ApiOperation("为某课程导入人员")
    @PostMapping("{courseId}/import")
    public Result importmem(@PathVariable("courseId")Integer courseId, @RequestBody String[] usernameList){
        userCourseService.addMembers(courseId, usernameList);
        return ResultFactory.buildSuccessResult("导入成功!");
    }

    @ApiOperation("删除课程成员")
    @PostMapping("{courseId}/remove")
    public Result remove(@PathVariable("courseId")Integer courseId, @RequestBody String[] usernameList){
        userCourseService.removeMembers(courseId, usernameList);
        return ResultFactory.buildSuccessResult("删除成功!");
    }

    @ApiOperation("课程模糊查询")
    @GetMapping("findlike")
    public Result findByCourseIdLike(@RequestParam String keyword,@RequestParam int type){
        if(type==0)
            return ResultFactory.buildSuccessResult(courseService.get(Integer.parseInt(keyword)));
        else
            return ResultFactory.buildSuccessResult(courseService.findlike(keyword));
    }

    @ApiOperation("开课或更新信息")
    @PostMapping("create")
    public Result create(@RequestBody Course course){
        int msg = courseService.addOrUpdate(course);
        if(msg==0){
            return ResultFactory.buildSuccessResult("创建成功！");
        }else if(msg==1){
            return ResultFactory.buildFailResult("课程号已存在！");
        }else{
            return ResultFactory.buildFailResult("课程名已存在！");
        }
    }

    @ApiOperation("教师发布公告")
    @CrossOrigin
    @PostMapping("notice")
    public  Result publish(@RequestBody @Valid Board board){
        boardService.publish(board);
        return ResultFactory.buildSuccessResult("更新公告信息成功");
    }

    @ApiOperation("教师删除公告信息")
    @CrossOrigin
    @PostMapping("deletenotice")
    public  Result deleteBoard(@RequestParam int boardId){
        boardService.deleteByBoardId(boardId);
        return ResultFactory.buildSuccessResult("删除公告成功");
    }
    @ApiOperation("获取课程公告")
    @CrossOrigin
    @GetMapping("{courseId}/board")
    public  Result getBoard(@PathVariable("courseId") Integer courseId){
        return ResultFactory.buildSuccessResult(boardService.findByCourseId(courseId));
    }
    @ApiOperation("设置责任教师")
    @CrossOrigin
    @PostMapping("{courseId}/setmain")
    public Result setmain(@PathVariable("courseId") Integer courseId,@RequestParam String username){
        courseService.setmain(courseId, username);
        return ResultFactory.buildSuccessResult("设置成功");
    }
    @ApiOperation("查看课程留言板")
    @CrossOrigin
    @GetMapping("{courseId}/fboard")
    public Result discussBoard(@PathVariable("courseId") Integer courseId){
        return ResultFactory.buildSuccessResult(courseService.getAllF(courseId));
    }
    @ApiOperation("添加留言")
    @CrossOrigin
    @PostMapping("feedback")
    public Result feedback(@RequestBody Feedback feedback){
        courseService.feedback(feedback);
        return ResultFactory.buildSuccessResult("反馈成功！");
    }
}
