package com.tan.labbackend.service;

import com.tan.labbackend.dao.UserContestDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tan.labbackend.entity.UserContest;

@Service
public class UserContestService {
    @Autowired
    UserContestDAO userContestDAO;

    @Transactional
    public void addOrUpdate(UserContest userContest){
        userContestDAO.save(userContest);
    }
}
