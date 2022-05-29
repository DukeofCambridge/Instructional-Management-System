package com.tan.labbackend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;

@Table(name = "report", indexes = {
        @Index(name = "project_id", columnList = "project_id"),
        @Index(name = "user_id", columnList = "user_id")
})
@Data
@Entity
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "title")
    private String title;

    @Lob
    @Column(name = "content_html")
    private String contentHtml;

    @Lob
    @Column(name = "content_md")
    private String contentMd;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "date")
    private Date date;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

}
