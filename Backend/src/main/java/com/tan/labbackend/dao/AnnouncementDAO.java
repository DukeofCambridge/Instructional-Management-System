package com.tan.labbackend.dao;

import com.tan.labbackend.entity.Announcement;
import com.tan.labbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnnouncementDAO extends JpaRepository<Announcement, Integer> {
    List<Announcement> findAllByUser(User user);
}