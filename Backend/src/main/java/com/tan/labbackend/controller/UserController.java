package com.tan.labbackend.controller;

import com.tan.labbackend.entity.User;
import com.tan.labbackend.dto.UserDTO;
import com.tan.labbackend.result.Result;
import com.tan.labbackend.result.ResultFactory;
import com.tan.labbackend.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 管理员用于用户信息管理
 */
@Api("管理员用于用户信息管理")
@RestController
//@CrossOrigin("*")
@CrossOrigin
@RequestMapping("api/admin/user")
public class UserController {
    @Autowired
    UserService userService;

    /**
     * 返回所有用户信息
     * @return
     */
    @ApiOperation("查看所有用户信息")
    @GetMapping("")
    @CrossOrigin
    public Result findAllUser() {
        List<User> users = userService.findAllUser();

        List<UserDTO> result = users
                .stream().map(user -> (UserDTO) new UserDTO().convertFor(user)).collect(Collectors.toList());
        return ResultFactory.buildSuccessResult(result);
    }

    /**
     * 查找用户信息
     * @return
     */
    @ApiOperation("根据用户名查看用户信息")
    @GetMapping("/{username}")
    public Result findUserByUsername(@PathVariable("username") String username) {
        username = HtmlUtils.htmlEscape(username);
        User user = userService.findByUsername(username);
        if(null==user){
            return ResultFactory.buildFailResult("用户不存在");
        }
        UserDTO result = new UserDTO().convertFor(user);
        return ResultFactory.buildSuccessResult(result);
    }

    /**
     * 查看所有未激活用户
     * @return
     */
    @ApiOperation("查看所有未激活用户")
    @GetMapping("/status/find")
    public  Result findAllNotActivatedUser(){
        return ResultFactory.buildSuccessResult(userService.findAllUser());
    }

    /**
     * 新用户注册
     * @param user
     * @return
     */
    @ApiOperation("新用户注册")
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        System.out.println("注册api已经调用啦");
        int status = userService.register(user);
        switch (status) {
            case 0:
                return ResultFactory.buildSuccessResult("注册成功");
            case 1:
                return ResultFactory.buildFailResult("用户名和姓名不能为空");
            case 2:
                return ResultFactory.buildFailResult("用户已存在");
            case 3:
                return ResultFactory.buildFailResult("邮箱格式不正确");
            case 4:
                return ResultFactory.buildFailResult("该邮箱已被注册");
        }
        return ResultFactory.buildFailResult("未知错误");
    }

    /**
     *
     * 更新用户状态,激活/未激活
     * @param user
     * {
     *     "username": "2323",
     *     "password": 1234,
     *     "enabled" : true,
     *     "phone":12346,
     *     "email":"233423@126.com",
     *     "role":3
     * }
     * @return
     */
    @CrossOrigin
    @ApiOperation("更新用户状态,激活/未激活")
    @PostMapping("/status")
    public Result updateUserStatus(@Valid @RequestBody User user){

        userService.updateUserEnabled(user.getUsername(),user.getEnabled());
        return ResultFactory.buildSuccessResult("更新用户状态成功");
    }

    /**
     * 更新用户信息
     * {
     *     "username": "2323",
     *     "password": 1234,
     *     "enabled" : true,
     *     "phone":12346,
     *     "email":"233423@126.com",
     * }
     * @param userDTO
     * @return
     */
    @ApiOperation("更新用户信息（邮箱、手机、角色）")
    @CrossOrigin
    @PostMapping("")
    public Result updateUserMsg(@Valid @RequestBody UserDTO userDTO){
        User user = userDTO.convertToUser();
        userService.updateUserMsg(user);
        return ResultFactory.buildSuccessResult("更新用户信息成功");
    }

    /**
     * 用户注销
     * {
     *     "username": "223",
     *     "password": 1234,
     *     "phone":12346,
     *     "email":"233423@126.com",
     *
     * }
     * @return
     */
    @CrossOrigin
    @ApiOperation("用户注销")
    @PostMapping("/delete")
    public  Result deleteUser(@Valid @RequestParam String username){

        userService.deleteUserByUsername(username);
        return ResultFactory.buildSuccessResult("用户注销成功");
    }

    @ApiOperation("用户模糊查询")
    @CrossOrigin
    @GetMapping("/findcontaining")
    public Result findByUsernameLike(@RequestParam Integer type, @RequestParam String keyword ){
        if(type==0){
            return ResultFactory.buildSuccessResult(userService.findAllByUsernameContaining(keyword));
        }else if(type==1){
            return ResultFactory.buildSuccessResult(userService.findAllByNameContaining(keyword));
        }else{
            return ResultFactory.buildFailResult("请选择正确的查询类型！");
        }
    }
}
