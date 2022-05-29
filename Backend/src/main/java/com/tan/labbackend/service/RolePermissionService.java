package com.tan.labbackend.service;

import com.tan.labbackend.entity.Permission;
import com.tan.labbackend.entity.RolePermission;
import com.tan.labbackend.dao.RolePermissionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class RolePermissionService {
    @Autowired
    RolePermissionDAO rolePermissionDAO;
    @Autowired
    RoleService roleService;
    /**
     * 返回该角色在角色权限表中所有记录
     * @param rid
     * @return
     */
    List<RolePermission> findAllByRoleID(int rid) {
        return rolePermissionDAO.findAllByRole(roleService.get(rid));
    }

    /**
     * 保存角色权限修改信息
     * @param rid
     * @param perms
     */
    @Transactional
    public void savePermissionChanges(int rid, List<Permission> perms){
        rolePermissionDAO.deleteAllByRole(roleService.get(rid));
        List<RolePermission> urs=new ArrayList<>();
        perms.forEach(role -> {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRole(roleService.get(rid));
            rolePermission.setPermission(role);
            urs.add(rolePermission);
        });
        rolePermissionDAO.saveAll(urs);
    }

}
