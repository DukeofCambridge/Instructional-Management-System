package com.tan.labbackend.controller;

import com.tan.labbackend.entity.Role;
import com.tan.labbackend.result.Result;
import com.tan.labbackend.result.ResultFactory;
import com.tan.labbackend.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api("角色管理")
@RestController
//@CrossOrigin("*")
@RequestMapping("api/admin/role")
public class RoleController {
    @Autowired
    RoleService roleService;
    /**
     * 返回所有角色信息
     * @return
     */
    @ApiOperation("查看所有角色信息")
    @GetMapping("")
    public Result findAllRole() {
        return ResultFactory.buildSuccessResult(roleService.findAll());
    }

    /**
     * 更新角色信息
     * @param role
     * @return
     */
    @ApiOperation("更新角色信息(名称、激活状态、权限）")
    @CrossOrigin
    @PostMapping("")
    public  Result updateRoleMsg(@RequestBody Role role){
        roleService.updateRoleMsg(role);
        return ResultFactory.buildSuccessResult("更新角色信息成功");
    }

    /**
     * 添加角色（名称、激活状态、权限）
     * @param role
     * @return
     */
    @ApiOperation("添加角色")
    @CrossOrigin
    @PostMapping("/add")
    public  Result addRole(@RequestBody Role role){
        roleService.add(role);
        return ResultFactory.buildSuccessResult("添加角色成功");
    }



}
