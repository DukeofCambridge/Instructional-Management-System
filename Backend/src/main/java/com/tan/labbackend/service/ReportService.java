package com.tan.labbackend.service;

import com.tan.labbackend.dao.ReportDAO;
import com.tan.labbackend.entity.Project;
import com.tan.labbackend.entity.Report;
import com.tan.labbackend.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class ReportService {
    @Autowired
    ReportDAO reportDAO;

    public Report get(int id){
        return reportDAO.findById(id).orElse(null);
    }
    public Report getReport(Project project, User user){
        if(null!=reportDAO.findAllByProjectAndUser(project, user)){
            return reportDAO.findAllByProjectAndUser(project, user).get(0);
        }else{
            return null;
        }
    }
    /**
     * 保存或更新报告
     * @param report
     */
    @Transactional
    public void addOrUpdate(Report report){
        reportDAO.save(report);
//        if(null!=reportDAO.findAllByProjectAndUser(report.getProject(), report.getUser())){
//            Report or = reportDAO.findAllByProjectAndUser(report.getProject(), report.getUser()).get(0);
//            Date date =new Date();
//            or.setDate(date);
//            or.setContentMd(report.getContentMd());
//            or.setContentHtml(report.getContentHtml());
//            reportDAO.save(or);
//        }else {
//            Report r = new Report();
//            r.setContentHtml(report.getContentHtml());
//            r.setContentMd(report.getContentMd());
//            r.setProject(report.getProject());
//            r.setUser(report.getUser());
//            Date date = new Date();
//            r.setDate(date);
//            reportDAO.save(r);
//        }
    }

    //删除报告
    @Transactional
    public void deleteByReportId(Integer rid){
        reportDAO.deleteById(rid);
    }
}
