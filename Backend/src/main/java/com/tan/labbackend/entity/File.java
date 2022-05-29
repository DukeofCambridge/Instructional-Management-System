package com.tan.labbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "file", indexes = {
        @Index(name = "course_id", columnList = "course_id"),
        @Index(name = "project_id", columnList = "project_id"),
        @Index(name = "user_id", columnList = "user_id")
})
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class File {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "filename")
    private String filename;

    @Column(name = "url")
    private String url;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}