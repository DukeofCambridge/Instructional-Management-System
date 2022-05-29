package com.tan.labbackend.dao;

import com.tan.labbackend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleDAO extends JpaRepository<Role, Integer> {
    Role deleteAllByEnabled(boolean enabled);
}
