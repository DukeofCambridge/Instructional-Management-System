package com.tan.labbackend.service;

import com.tan.labbackend.entity.Permission;
import com.tan.labbackend.entity.Role;
import com.tan.labbackend.entity.RolePermission;
import com.tan.labbackend.dao.PermissionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PermissionService {
    @Autowired
    PermissionDAO permissionDAO;
    @Autowired
    RoleService roleService;
    @Autowired
    UserService userService;
    @Autowired
    RolePermissionService rolePermissionService;


    /**
     * 返回所有权限类型
     * @return
     */
    public List<Permission> findAll() {
        return permissionDAO.findAll();
    }

    /**
     * 根据角色id查找所有权限表记录
     * @param rid
     * @return
     */
    public List<Permission> listPermsByRoleId(int rid) {
        return rolePermissionService.findAllByRoleID(rid)
                .stream().map(RolePermission::getPermission).collect(Collectors.toList());
    }
    /**
     * 根据用户名查找所有权限uml
     * @param username
     * @return
     */
    public Set<String> listPermissionURLsByUser(String username) {
        Role role = userService.findByUsername(username).getRole();

        List<Permission> perms = rolePermissionService.findAllByRoleID(role.getId())
                .stream().map(RolePermission::getPermission).collect(Collectors.toList());

        Set<String> URLs = perms.stream().map(Permission::getUrl).collect(Collectors.toSet());

        return URLs;
    }
    /**
     * 更新权限信息
     * @param permission
     * @param
     */
    public void updatePermissionMsg(Permission permission) {
        Permission permissionID = permissionDAO.findById(permission.getId()).orElse(null);
        permissionID.setName(permission.getName());
        permissionDAO.save(permissionID);
    }

    /**
     * 添加权限
     * @param permission
     */
    public void add(Permission permission) {
        permissionDAO.save(permission);
    }
    /**
     * 判断用户请求接口的是否在权限列表
     * @param requestAPI
     * @return
     */
    public boolean needFilter(String requestAPI) {
        List<Permission> ps = permissionDAO.findAll();
        for (Permission p: ps) {
            if (p.getUrl().equals(requestAPI)) {
                return true;
            }
        }
        return false;
    }

}
