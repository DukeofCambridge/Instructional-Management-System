package com.tan.labbackend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;

@Table(name = "project", indexes = {
        @Index(name = "course_id1_idx", columnList = "course_id")
})
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id", nullable = false)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "project_name", length = 45)
    private String projectName;

    @Column(name = "project_require")
    private String projectRequire;

    @Column(name = "start_time")
    private Instant startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "end_time")
    private Date endTime;

    @Column(name = "project_proportion")
    private Double projectProportion;

    @Column(name = "publisher", length = 45)
    private String publisher;

    @Column(name="type")
    private Integer type;
}
