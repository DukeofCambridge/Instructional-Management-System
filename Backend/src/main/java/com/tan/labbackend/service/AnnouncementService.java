package com.tan.labbackend.service;

import com.tan.labbackend.dao.AnnouncementDAO;
import com.tan.labbackend.entity.Announcement;
import com.tan.labbackend.entity.Board;
import com.tan.labbackend.entity.Course;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
@Service
public class AnnouncementService {
    @Autowired
    AnnouncementDAO announcementDAO;
    @Autowired
    UserService userService;
    public List<Announcement> findALl(){
        return announcementDAO.findAll();
    }
    public List<Announcement> findAllbyUserId(Integer uid){
        return announcementDAO.findAllByUser(userService.get(uid));
    }
    @Transactional
    public void addOrUpdate(Announcement announcement){
        announcementDAO.save(announcement);
    }
    @Transactional
    public void deleteByAnnouncementId(Integer bid){
        announcementDAO.deleteById(bid);
    }
}
