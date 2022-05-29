package com.tan.labbackend.controller;

import com.tan.labbackend.entity.User;
import com.tan.labbackend.dao.UserDAO;
import com.tan.labbackend.dto.UserDTO;
import com.tan.labbackend.result.Result;
import com.tan.labbackend.result.ResultFactory;
import com.tan.labbackend.service.UserService;
import com.tan.labbackend.utils.RabbitProducer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.mail.MessagingException;
import javax.validation.Valid;

@Api("登陆注册管理的接口文档")
@RestController
@RequestMapping("api/")
public class LoginController {
    @Autowired
    UserService userService;
    @Autowired
    UserDAO userDAO;

    @Autowired
    private RabbitProducer rabbitProducer;

    /**
     * 用户登陆（账号密码即可）
     * @param requestUser
     * @return
     */
    @ApiOperation("用户登陆（账号+密码）")
    @CrossOrigin
    @PostMapping("login")
    public Result login(@RequestBody User requestUser){
        String username= requestUser.getUsername();
        username= HtmlUtils.htmlEscape(username);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, requestUser.getPassword());
        usernamePasswordToken.setRememberMe(true);
        try {
            subject.login(usernamePasswordToken);
            User user = userService.findByUsername(username);
            if (!user.getEnabled()) {
                subject.logout();
                return ResultFactory.buildFailResult("您的账户尚未激活");
            }
            Integer role = user.getRole().getId();
            System.out.println("登录api调用啦  "+role);
            UserDTO result = new UserDTO().convertFor(user);
            return ResultFactory.buildSuccessResult(result);
        } catch (AuthenticationException e) {
            String message = "账号或密码错误";
            return ResultFactory.buildFailResult(message);
        }
    }

    /**
     * 用户退出登陆
     * @return
     */
    @ApiOperation("用户退出登陆")
    @GetMapping("logout")
    public Result logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        String message = "成功登出";
        return ResultFactory.buildSuccessResult(message);
    }

    @ApiOperation("发送激活邮件")
    @PostMapping("sendEmail")
    public Result sendEmail(@RequestParam String username){
//        // 检查邮箱是否已注册
//        User user = userDAO.findByEmail(email);
//        if(null!=user){
//            return ResultFactory.buildFailResult("该邮箱已被注册！");
//        }
        if(null==userDAO.findByUsername(username)){
            return ResultFactory.buildFailResult("该账号不存在！");
        }
        if(null==userDAO.findByUsername(username).getEmail()){
            return ResultFactory.buildFailResult("账号未绑定邮箱！");
        }
        String email = userDAO.findByUsername(username).getEmail();
        // 开启一个新线程发送邮件，防止用户长时间等待
        new Thread(() -> {
            try {
                rabbitProducer.sendFanout(email);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        return ResultFactory.buildSuccessResult("已发送激活邮件");
    }
    @ApiOperation("验证激活码")
    @PostMapping("verify")
    public Result verify(@RequestParam String emailToken){

        if(userService.verify(emailToken)){
            return ResultFactory.buildSuccessResult("验证码核验正确！");
        }
        return ResultFactory.buildFailResult("验证码已过期或不正确！");
    }
    @ApiOperation("获取全局身份")
    @GetMapping("identity")
    public Result identity(@RequestParam String username){
        return ResultFactory.buildSuccessResult(userDAO.findByUsername(username).getRole());
    }
    /**
     * 修改密码
     * @param user
     * @return
     */
    @CrossOrigin
    @ApiOperation("用户修改密码")
    @PostMapping("password")
    public Result updatePassword(@Valid @RequestBody User user) {
        userService.updatePassword(user);
        return ResultFactory.buildSuccessResult("修改密码成功");
    }
    @ApiOperation("更新手机、邮箱")
    @CrossOrigin
    @PostMapping("updateInfo")
    public Result updateInfo(@Valid @RequestBody User user){
        int status = userService.updateUserMsg(user);
        switch (status) {
            case 0:
                return ResultFactory.buildSuccessResult("修改成功");
            case 1:
                return ResultFactory.buildFailResult("邮箱格式不正确！");
            case 2:
                return ResultFactory.buildFailResult("该邮箱已被使用！");
        }
        return ResultFactory.buildFailResult("未知错误");
    }
}
