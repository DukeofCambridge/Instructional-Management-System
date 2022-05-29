package com.tan.labbackend.service;

import com.tan.labbackend.entity.Role;
import com.tan.labbackend.entity.User;
import com.tan.labbackend.dao.UserDAO;
import com.tan.labbackend.redis.RedisService;
import com.tan.labbackend.utils.RabbitProducer;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RabbitListener(queues = "fanout.a")
public class UserService {
    @Autowired
    UserDAO userDAO;
    @Autowired
    RedisService redisService;
    @Autowired
    RoleService roleService;
    @Autowired
    private RabbitProducer rabbitProducer;

    public List<User> findAllUser(){
        return userDAO.findAll();
    }
    /**
     * 用户是否存在
     * @param username
     * @return
     */
    public boolean isExist(String username) {
        User user = findByUsername(username);
        return null!=user;
    }

    /**
     * 根据用户名查找用户
     * @param username
     * @return
     */
    public User findByUsername(String username) {
        User user=userDAO.findByUsername(username);
        return user;
    }
    public List<User> findAllUserByEnabled(boolean enable){
        return userDAO.findAllByEnabled(enable);
    }

    public User get(int uid){
        User user = userDAO.findById(uid).orElse(null);
        return user;
    }

    public void add(User user) {
        userDAO.save(user);
    }

    /**
     * 修改用户信息
     * @param user
     */
    public int updateUserMsg(User user) {
        if(!user.getEmail().matches("^\\w+@(\\w+\\.)+\\w+$")){
            return 1;
        }
        // 邮箱已注册
        User testuser = userDAO.findByEmail(user.getEmail());
        User userID = userDAO.findByUsername(user.getUsername());
        if(null!=testuser&&testuser!=userID){
            return 2;
        }

        userID.setPhone(user.getPhone());
        userID.setEmail(user.getEmail());
//        userID.setName(user.getName());
        userDAO.save(userID);
        return 0;
//        userRoleService.saveRoleChanges(user.getId(),user.getUserRoles());
    }
    /**
     * 更新用户状态
     * @param username
     * @param enabled
     */
    public void updateUserEnabled(String username, boolean enabled) {
        User user = userDAO.findByUsername(username);
        user.setEnabled(enabled);
        userDAO.save(user);
    }
    /**
     * 账户注册
     */
    public int register(User user) {
        String username = user.getUsername();
        String email = user.getEmail();
//        String password = user.getPassword();
        // 默认初始密码为账号
        String password = user.getUsername();
        String name = user.getName();
        username = HtmlUtils.htmlEscape(username);
        user.setUsername(username);
//        phone = HtmlUtils.htmlEscape(phone);
//        user.setPhone(phone);
        email = HtmlUtils.htmlEscape(email);
        user.setEmail(email);
        name = HtmlUtils.htmlEscape(name);
        user.setName(name);
        // 管理员注册用户时默认 未激活
        user.setEnabled(false);

        Role role = roleService.get(user.getRole().getId());
        user.setRole(role);
        // 用户名和姓名不能为空
        if (username.equals("") || name.equals("")) {
            return 1;
        }
        boolean exist = isExist(username);
        // 用户名已存在
        if (exist) {
            return 2;
        }
        //利用正则表达式（可改进）验证邮箱是否符合邮箱的格式
        if(!email.matches("^\\w+@(\\w+\\.)+\\w+$")){
            return 3;
        }
        // 邮箱已注册
        User testuser = userDAO.findByEmail(email);
        if(null!=testuser){
            return 4;
        }



        // 默认生成 16 位盐
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times = 2;
        String encodedPassword = new SimpleHash("md5", password, salt, times).toString();

        user.setSalt(salt);
        user.setPassword(encodedPassword);

        userDAO.save(user);

        return 0;
    }

    /**
     * 验证邮箱
     */
    @RabbitHandler
    public int sendEmail(String email) throws MessagingException {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,true,"GBK");
        mailSender.setHost("smtp.qq.com");
        mailSender.setUsername("1162524575@qq.com");
        mailSender.setPassword("gctijyltttfrhaib");
        // 生成激活码
//        String code = CodeUtil.generateUniqueCode();
        // 使用redis缓存取代数据库存储
        String emailToken = getEmailToken(email);
        helper.setFrom("1162524575@qq.com");
        helper.setTo(email);
        helper.setSubject("账号激活");// 标题
        // 邮件内容为网页格式
        String content = "<html><head></head><body><h2>欢迎使用同济大学实验教学管理系统！</h2><h5>您的验证码为" + emailToken + " ,请您于5分钟内完成激活</h5></body></html>";
        helper.setText(content, true);// 内容
        try {
            mailSender.send(message);
//            saveCode(code);
//            User user = userDAO.findByEmail(email);
//            user.setCode(code);
//            userDAO.save(user);
            return 0;
        }catch (MailSendException e){
            return 1;
        } catch (Exception e) {
            return 2;
        }
    }

//    /**
//     * 根据激活码激活用户账号
//     */
//    public void activate(String code){
//        User user = userDAO.findByCode(code);
//        user.setEnabled(true);
//        userDAO.save(user);
//    }

    /**
     * 生成8位验证码
     */
    public String getEmailToken(String email){
        //
        UUID id=UUID.randomUUID();
        String[] idd=id.toString().split("-");
        String token = idd[0];
        // 放入缓存数据库
        redisService.set(token, email);
        // 设置5min过期
        redisService.expire(token,300, TimeUnit.SECONDS);
        return token;
    }
    /**
     * 验证令牌
     */
    public boolean verify(String emailToken) {
        Object email = redisService.get(emailToken);

        //            User user = userDAO.findByEmail(email.toString());
        //            user.setEnabled(true);
        //            userDAO.save(user);
        return email != null;
    }
    /**
     * 用户注销
     * @param username
     */
    @Transactional
    public void deleteUserByUsername(String username){userDAO.deleteUserByUsername(username);}

    /**
     * 密码修改
     * @param user
     * @return
     */
    @Transactional
    public User updatePassword(User user) {
        User userInDB = userDAO.findByUsername(user.getUsername());
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times = 2;
        String encodedPassword = new SimpleHash("md5", user.getPassword(), salt, times).toString();
        userInDB.setSalt(salt);
        userInDB.setPassword(encodedPassword);
        // 首次登录修改密码+激活账户
        userInDB.setEnabled(true);
        return userDAO.save(userInDB);
    }

    public List<User> findAllByUsernameContaining(String keyword){
        return userDAO.findAllByUsernameContaining(keyword);
    }
    public List<User> findAllByNameContaining(String keyword){
        return userDAO.findAllByNameContaining(keyword);
    }

}

