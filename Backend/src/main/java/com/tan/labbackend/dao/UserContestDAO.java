package com.tan.labbackend.dao;

import com.tan.labbackend.entity.UserContest;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tan.labbackend.entity.UserContestId;

public interface UserContestDAO extends JpaRepository<UserContest, UserContestId> {
}