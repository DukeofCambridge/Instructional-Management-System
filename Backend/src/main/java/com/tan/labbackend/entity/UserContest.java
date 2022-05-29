package com.tan.labbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Table(name = "user_contest", indexes = {
        @Index(name = "contest_id", columnList = "contest_id")
})
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@IdClass(UserContestId.class)
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class UserContest {
    @Id
    @Column(name = "user_id", nullable = false)
    private Integer userId;
    @Id    
    @Column(name = "contest_id", nullable = false)
    private Integer contestId;

    @Column(name = "submit_time")
    private Instant submitTime;

    @Column(name = "project_grade")
    private Double projectGrade;

}