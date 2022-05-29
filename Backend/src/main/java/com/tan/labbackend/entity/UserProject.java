package com.tan.labbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Table(name = "user_project", indexes = {
        @Index(name = "student_id1_idx", columnList = "student_id")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@Entity
@IdClass(UserProjectId.class)
public class UserProject {
    @Id
    @Column(name = "project_id")
    Integer projectId;
    @Id
    @Column(name = "student_id")
    Integer studentId;

    @Column(name = "project_grade")
    private Double projectGrade;

    @Column(name = "submit_time")
    private Instant submitTime;

    @Column(name = "url", length = 45)
    private String url;

}
