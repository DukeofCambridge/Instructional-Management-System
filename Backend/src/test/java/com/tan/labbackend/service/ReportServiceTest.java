package com.tan.labbackend.service;

import com.tan.labbackend.dao.ReportDAO;
import com.tan.labbackend.entity.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

class ReportServiceTest {
    @Mock
    ReportDAO reportDAO;

    private ReportService reportService;

    private static List<Project> projects;

    private static Report report;

    private static User user;

    @BeforeEach
    void setup() {
        reportService = new ReportService(reportDAO);


        Calendar calendar = Calendar.getInstance();

        projects = new ArrayList<>();

        Project project = new Project();
        project.setId(1);
        project.setCourse(new Course(42034203, "SE", "Learn common software management", "Teacher Ruan"));
        project.setProjectName("Process model");
        project.setProjectRequire("null");
        project.setStartTime(null);
        calendar.set(2023, Calendar.JANUARY, 1);
        project.setEndTime(calendar.getTime());
        project.setProjectProportion(30.0D);
        project.setPublisher("Teacher Huang");
        project.setType(2);

        projects.add(project);

        Project project1 = new Project();
        project1.setId(2);
        project1.setCourse(new Course(42034203, "SE", "Learn common software management", "Teacher Ruan"));
        project1.setProjectName("Confrontation exercises");
        project1.setProjectRequire("");
        project1.setStartTime(null);
        calendar.set(2023, Calendar.MAY, 1);
        project1.setEndTime(calendar.getTime());
        project1.setProjectProportion(20.0D);
        project1.setPublisher("Teacher Huang");
        project1.setType(0);

        projects.add(project1);

        Project project2 = new Project();
        project2.setId(3);
        project2.setCourse(new Course(42034203, "SE", "Learn common software management", "Teacher Ruan"));
        project2.setProjectName("Small experiments");
        project2.setProjectRequire("Software engineering prefix knowledge");
        project2.setStartTime(null);
        calendar.set(2022, Calendar.MAY, 1);
        project2.setEndTime(calendar.getTime());
        project2.setProjectProportion(10.0D);
        project2.setPublisher("Teacher Ruan");
        project2.setType(0);

        projects.add(project2);

        //User
        user = new User();
        user.setId(0);
        user.setUsername("1952111");
        user.setPassword("");
        user.setSalt("");
        user.setPhone("");
        user.setEmail("");
        user.setEnabled(false);
        user.setName("");
        user.setRole(new Role());


        // Report
        report = new Report();
        report.setId(1);
        report.setTitle("SE Report");
        report.setContentHtml("Test Content");
        report.setContentMd("");
        report.setUser(new User());
        report.setDate(new Date());
        report.setProject(projects.get(0));
    }

    @Test
    @Order(1)
    void shouldGetReport() throws Exception {
        final Integer reportId = 1;

        Mockito.when(reportDAO.findById(reportId)).thenReturn(Optional.ofNullable(report));

        Report reportGet = reportService.get(reportId);
        Assertions.assertThat(reportGet.getId()).isEqualTo(report.getId());
        Assertions.assertThat(reportGet.getTitle()).isEqualTo("SE Report");
    }

    @Test
    @Order(2)
    void shouldGetReportBySearch() throws Exception {
        ArrayList<Report> resReport = new ArrayList<>();
        resReport.add(report);

        Project project = new Project();

        Report emptyReport = new Report();
        emptyReport.setTitle("");
        ArrayList<Report> resEmptyReport = new ArrayList<>();
        resEmptyReport.add(emptyReport);

        lenient().when(reportDAO.findAllByProjectAndUser( eq(projects.get(0)), eq(user))).thenReturn(resReport);
        lenient().when(reportDAO.findAllByProjectAndUser( eq(project), eq(user))).thenReturn(resEmptyReport);

        Report reportGet = reportService.getReport(projects.get(0), user);

        Assertions.assertThat(reportGet.getId()).isEqualTo(ReportServiceTest.report.getId());
        Assertions.assertThat(reportGet.getTitle()).isEqualTo("SE Report");

        Report EmptyGet = reportService.getReport(project, user);
        Assertions.assertThat(EmptyGet.getTitle()).isEqualTo("");



    }

}