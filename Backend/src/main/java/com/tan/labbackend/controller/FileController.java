package com.tan.labbackend.controller;


import com.alibaba.fastjson.serializer.IntegerCodec;
import com.tan.labbackend.dao.FileDAO;
import com.tan.labbackend.dao.UserProjectDAO;
import com.tan.labbackend.entity.File;
import com.tan.labbackend.entity.User;
import com.tan.labbackend.entity.UserProject;
import com.tan.labbackend.result.Result;
import com.tan.labbackend.result.ResultFactory;
import com.tan.labbackend.service.FileService;
import com.tan.labbackend.service.UserService;
import com.tan.labbackend.utils.FtpUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.checkerframework.checker.units.qual.Area;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Api("文件管理")
@RestController
//@CrossOrigin("*")
@RequestMapping("api/file/")
public class FileController {
    @Autowired
    FileService fileService;
    @Autowired
    FileDAO fileDAO;
    @Autowired
    UserProjectDAO projectDAO;
    @Autowired
    UserService userService;

    /**
     * 上传文件（还需与页面进行完善）
     *
     * @param multipartFile
     * @return
     */
    @ApiOperation("文件上传")
    @PostMapping("upload")
    public Result Fileupload(@RequestParam("headPic") MultipartFile multipartFile, @RequestParam("cour_id") Integer cour_id, @RequestParam("pro_id") Integer pro_id, @RequestParam("stu_id") Integer stu_id) throws Exception {
        boolean fileName = FtpUtils.upload(multipartFile, cour_id, pro_id, stu_id);
//        List<Project> allById = projectDAO.findAllById(pro_id);
//        Project byId = projectGradeDAO.findByProjectId(pro_id);
        if (fileName) {
            UserProject userProject= projectDAO.findByProjectIdAndStudentId(pro_id,stu_id);
            userProject.setUrl(multipartFile.getOriginalFilename());
            projectDAO.save(userProject);
            System.out.println("上传成功");
            return ResultFactory.buildSuccessResult("上传成功");
        }
        return ResultFactory.buildFailResult("上传失败");
    }

    /**
     * 下载文件
     *
     * @param fileName
     * @return
     * @throws Exception
     */
    @ApiOperation("文件下载")
    @PostMapping("download")
    public Result Filedownload(@RequestParam("cour_id") Integer cour_id,@RequestParam("pro_id") Integer pro_id, @RequestParam("stu_name") String stu_name) throws Exception {
//        byte[] arraybyte = FtpUtils.downFileByte("/home/hnc/files","1-自选型综合实验管理规范.doc");
        User user=userService.findByUsername(stu_name);
        Integer stu_id= user.getId();
        String remotePath = "/home/hnc/files"+"/" + cour_id +"/xiangmu" +"/"+ pro_id + "/" + stu_id + "/";
        UserProject userProject= projectDAO.findByProjectIdAndStudentId(pro_id,stu_id);
        byte[] arraybyte = FtpUtils.downFileByte(remotePath, userProject.getUrl());
        if (arraybyte != null) {
            System.out.println("下载成功");
            return ResultFactory.buildSuccessResult("下载成功");
        }
        return ResultFactory.buildFailResult("下载失败");
    }


    /**
     * 上传文件（还需与页面进行完善）
     *
     * @param multipartFile
     * @return
     */
    @ApiOperation("资料文件上传")
    @PostMapping("dataupload")
    public Result Filedataupload(@RequestParam("headPic") MultipartFile multipartFile, @RequestParam("cour_id") Integer cour_id, @RequestParam("pro_id") Integer pro_id,@RequestParam("user_id") Integer user_id) throws Exception {
        boolean fileName = FtpUtils.dataupload(multipartFile, cour_id, pro_id);
        String file = new String(multipartFile.getOriginalFilename());
        System.out.println(file);
        String fileurl="files" + "/" + cour_id + "/ziliao/";
        if (fileName) {
            fileService.saveurl( file, fileurl, cour_id,pro_id, user_id);
            System.out.println("上传成功");
            return ResultFactory.buildSuccessResult("上传成功");
        }
        return ResultFactory.buildFailResult("上传失败");
    }

    @ApiOperation("下载资料文件")
    @GetMapping("datadownload/{file_id}")
    public Result Filedatadownload(@PathVariable("file_id")Integer file_id){
        File file=fileDAO.findFileById(file_id);
        byte[] arraybyte = new byte[0];
        try {
            arraybyte = FtpUtils.downFileByte(file.getUrl().toString(), file.getFilename().toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultFactory.buildFailResult("下载失败");
        }
        if (arraybyte != null) {
            System.out.println("下载成功");
            return ResultFactory.buildSuccessResult("下载成功");
        }
        return ResultFactory.buildFailResult("下载失败");
    }


    @ApiOperation("查询项目所有文件资料")
    @GetMapping("{projectId}/all")
    public Result getallProjects(@PathVariable("projectId") Integer projectId) {
        return ResultFactory.buildSuccessResult(fileService.getFiles(projectId));
    }
    @ApiOperation("删除资料")
    @PostMapping("delete")
    public Result deletefile(@RequestParam("file_id") Integer file_id) throws Exception {
        File file=fileDAO.findFileById(file_id);
        boolean b = FtpUtils.deleteFileByte(file.getUrl().toString(), file.getFilename().toString());
//        boolean b = FtpUtils.deleteFileByte("/home/hnc/files/2/ziliao/", "1953060-hnc.txt");

        if(b){
            fileDAO.delete(file);
        }
        return ResultFactory.buildSuccessResult(b);
    }



    @ApiOperation("缓存文件")
    @GetMapping("datacache/{file_id}")
    public Result Filedatacache(@PathVariable("file_id")Integer file_id){
        File file=fileDAO.findFileById(file_id);
        byte[] arraybyte = new byte[0];
        try {
            arraybyte = FtpUtils.FileByte(file.getUrl().toString(), file.getFilename().toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultFactory.buildFailResult("下载失败");
        }
        if (arraybyte != null) {
            System.out.println("下载成功");
            return ResultFactory.buildSuccessResult("下载成功");
        }
        return ResultFactory.buildFailResult("下载失败");
    }


    @ApiOperation("删除缓存资料")
    @GetMapping("cache/{filename}")
    public Result getallProjects(@PathVariable("filename") String filename) {
        Boolean req=FtpUtils.delfile(filename);
        return ResultFactory.buildSuccessResult(req);
    }
}
