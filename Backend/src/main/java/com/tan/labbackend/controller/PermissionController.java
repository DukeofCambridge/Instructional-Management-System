package com.tan.labbackend.controller;

import com.tan.labbackend.entity.Permission;
import com.tan.labbackend.result.Result;
import com.tan.labbackend.result.ResultFactory;
import com.tan.labbackend.service.PermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api("权限管理")
@RestController
//@CrossOrigin("*")
@RequestMapping("api/admin/permission")
public class PermissionController {
    @Autowired
    PermissionService permissionService;
    /**
     * 返回所有权限信息
     * @return
     */
    @ApiOperation("查看所有权限信息")
    @GetMapping("")
    public Result findAllPermission() {
        return ResultFactory.buildSuccessResult(permissionService.findAll());
    }

    /**
     * 更新权限信息
     * @param permission
     * @return
     */
    @ApiOperation("更新权限信息")
    @CrossOrigin
    @PostMapping("")
    public  Result updatePermissionMsg(@RequestBody Permission permission){
        permissionService.updatePermissionMsg(permission);
        return ResultFactory.buildSuccessResult("更新角色信息成功");
    }

    /**
     * 添加角色（名称、激活状态、权限）
     * @param permission
     * @return
     */
    @ApiOperation("添加角色")
    @CrossOrigin
    @PostMapping("/add")
    public  Result addPermission(@RequestBody Permission permission){
        permissionService.add(permission);
        return ResultFactory.buildSuccessResult("添加角色成功");
    }


}
