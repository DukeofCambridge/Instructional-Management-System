package com.tan.labbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "user_course", indexes = {
        @Index(name = "course_id", columnList = "course_id"),
        @Index(name = "id_idx", columnList = "user_id")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@Entity
@IdClass(UserCourseId.class)
public class UserCourse {
    @Id
    @Column(name = "course_id", nullable = false)
    private Integer courseId;

    @Id
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "project_grade")
    private Double projectGrade;

    @Column(name = "attendance_grade")
    private Integer attendanceGrade;

}