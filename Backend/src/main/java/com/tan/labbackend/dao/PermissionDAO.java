package com.tan.labbackend.dao;

import com.tan.labbackend.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionDAO extends JpaRepository<Permission, Integer> {
}