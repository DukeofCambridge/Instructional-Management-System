package com.tan.labbackend.dao;

import com.tan.labbackend.entity.Role;
import com.tan.labbackend.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.beans.Transient;
import java.util.List;

public interface RolePermissionDAO extends JpaRepository<RolePermission, Integer> {
    List<RolePermission> findAllByRole(Role role);
    List<RolePermission> findAllByRoleIn(List<Role> roles);
    @Transient
    void deleteAllByRole(Role role);
}
