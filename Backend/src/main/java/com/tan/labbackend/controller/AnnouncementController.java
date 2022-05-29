package com.tan.labbackend.controller;

import com.tan.labbackend.entity.Announcement;
import com.tan.labbackend.entity.Board;
import com.tan.labbackend.entity.Project;
import com.tan.labbackend.entity.Role;
import com.tan.labbackend.result.Result;
import com.tan.labbackend.result.ResultFactory;
import com.tan.labbackend.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Api("公告板管理")
@RestController
//@CrossOrigin("*")
@RequestMapping("api/announcement/")
public class AnnouncementController {
    @Autowired
    AnnouncementService announcementService;
    /**
     * 返回所有角色信息
     * @return
     */
    @ApiOperation("查看所有角色信息")
    @GetMapping("")
    public Result findAllAnnouncement() {
        return ResultFactory.buildSuccessResult(announcementService.findALl());
    }

    /**
     * 添加公告板
     * @param announcement
     * @return
     */
    @ApiOperation("添加公告")
    @CrossOrigin
    @PostMapping("/add")
    public  Result addAnnouncement(@RequestBody Announcement announcement){
        announcementService.addOrUpdate(announcement);
        return ResultFactory.buildSuccessResult("添加公告成功");
    }

    /**
     * 删除公告
     * @param announcementId
     * @return
     */
    @ApiOperation("添加公告")
    @CrossOrigin
    @PostMapping("/delete")
    public  Result deleteAnnouncement(@RequestParam Integer announcementId){
        announcementService.deleteByAnnouncementId(announcementId);
        return ResultFactory.buildSuccessResult("删除公告成功");
    }
}
