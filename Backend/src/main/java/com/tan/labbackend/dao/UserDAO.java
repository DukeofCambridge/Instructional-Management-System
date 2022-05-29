package com.tan.labbackend.dao;

import com.tan.labbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDAO extends JpaRepository<User,Integer> {
    User findByUsername(String username);
    List<User> findAllByEnabled(boolean enable);
    void deleteUserByUsername(String username);
    User findByEmail(String email);
    // 模糊查询
    List<User> findAllByUsernameContaining(String keyword);
    List<User> findAllByNameContaining(String keyword);
}
