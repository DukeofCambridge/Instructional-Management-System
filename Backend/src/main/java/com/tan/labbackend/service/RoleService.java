package com.tan.labbackend.service;

import com.tan.labbackend.entity.Role;
import com.tan.labbackend.dao.RoleDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    @Autowired
    RoleDAO roleDAO;
    @Autowired
    RoleService roleService;
    @Autowired
    UserService userService;
    @Autowired
    RolePermissionService rolePermissionService;
    @Autowired
    PermissionService permissionService;


    /**
     * 返回所有角色类型
     * @return
     */
    public List<Role> findAll() {
        List<Role> roles= roleDAO.findAll();
        for (Role role : roles) {
            role.setPerms(permissionService.listPermsByRoleId(role.getId()));
        }
        return roles;
    }

    public Role get(int rid){
        Role role = roleDAO.findById(rid).orElse(null);
        return role;
    }

    /**
     * 更新角色信息
     * @param role
     * @param
     */
    public void updateRoleMsg(Role role) {
        Role roleID = roleDAO.findById(role.getId()).orElse(null);
        roleID.setName(role.getName());
        roleID.setEnabled(role.getEnabled());
        roleDAO.save(roleID);
        rolePermissionService.savePermissionChanges(role.getId(),role.getPerms());
    }

    /**
     * 添加角色
     * @param role
     */
    public void add(Role role) {
        roleDAO.save(role);
        rolePermissionService.savePermissionChanges(role.getId(),role.getPerms());
    }

    

}
