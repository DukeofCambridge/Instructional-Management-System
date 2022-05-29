package com.tan.labbackend.service;


import com.tan.labbackend.dao.FileDAO;
import com.tan.labbackend.entity.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {
    @Autowired
    FileDAO fileDAO;
    @Autowired
    CourseService courseService;
    @Autowired
    UserService userService;
    @Autowired
    ProjectService projectService;

    public int saveurl(String filename,String fileurl,Integer cour_id,Integer pro_id,Integer user_id){
        File file=new File();
        file.setFilename(filename);
        String fileurlall="/home/hnc/"+fileurl;
        file.setUrl(fileurlall);
//        Course coursef=courseService.get(cour_id);
        file.setCourse(courseService.get(cour_id));
        file.setProject(projectService.get(pro_id));
        file.setUser(userService.get(user_id));
//        file.setId(1);
        fileDAO.save(file);
        return 0;
    }
    // 查询所有文件
    public List<File> getFiles(Integer projectId){
        return fileDAO.findAllByprojectId(projectId);
    }

}
