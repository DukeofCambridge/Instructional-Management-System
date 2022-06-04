package com.tan.labbackend.service;

import com.tan.labbackend.dao.ProjectDAO;
import com.tan.labbackend.dao.UserProjectDAO;
import com.tan.labbackend.entity.Course;
import com.tan.labbackend.entity.Project;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {
    @Mock
    ProjectDAO projectDAO;
    @Mock
    UserCourseService userCourseService;
    @Mock
    UserProjectDAO userProjectDAO;

    private ProjectService projectService;


    private static List<Project> projects;

    @BeforeEach
    void setup() {
        projectService = new ProjectService(projectDAO, userCourseService, userProjectDAO);

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
    }

    @Test()
    void shouldGetPastProjects() throws Exception {
        final Integer courseId = 42034203;

        List<Project> pastProjects = new ArrayList<>();
        for (Project p : projects) {
            System.out.println(p);
            if (p.getEndTime().before(new Date())) {
                pastProjects.add(p);
            }
        }
        System.out.println(pastProjects);

        lenient().when(projectDAO.findByCourseIdAndEndTimeBefore(eq(courseId), any(Date.class))).thenReturn(pastProjects);
        List<Project> res = projectService.getPastProjects(courseId);
        System.out.println(res);
        Assertions.assertThat(res.get(0).getId()).isEqualTo(pastProjects.get(0).getId());

    }

    @Test
    void shouldGetNowProjects() throws Exception {

        final Integer courseId = 42034203;

        List<Project> nowProjects = new ArrayList<>();
        for (Project p : projects) {
            System.out.println(p);
            if (p.getEndTime().after(new Date())) {
                nowProjects.add(p);
            }
        }
        System.out.println(nowProjects);

        lenient().when(projectDAO.findByCourseIdAndEndTimeAfter(eq(courseId), any(Date.class))).thenReturn(nowProjects);
        List<Project> res = projectService.getCurrentProjects(courseId);
        System.out.println(res);

        Assertions.assertThat(res.get(0).getId()).isEqualTo(1);
        Assertions.assertThat(res.get(1).getId()).isEqualTo(2);

    }

    @Test
    void shouldGetAllProjects() throws Exception {

        final Integer courseId = 42034203;
        Mockito.when(projectDAO.findAllByCourseId(courseId)).thenReturn(projects);

        List<Project> res = projectService.getAll(courseId);
        System.out.println(res);

        Assertions.assertThat(res.get(0).getId()).isEqualTo(projects.get(0).getId());
        Assertions.assertThat(res.get(1).getProjectName()).isEqualTo("Confrontation exercises");
        Assertions.assertThat(res.get(2).getProjectName()).isEqualTo("Small experiments");

    }

    @Test
    void shouldGetProjectDetail() throws Exception {
        final Integer projectId = 2;

        Mockito.when(projectDAO.findById(projectId)).thenReturn(Optional.ofNullable(projects.get(1)));

        Project res = projectService.getInfo(projectId);
        System.out.println(res);

        Assertions.assertThat(res.getProjectName()).isEqualTo("Confrontation exercises");
    }


    // 待办
    @Test
    void shouldPublishProject() throws Exception {
        Calendar calendar = Calendar.getInstance();

        Project project = new Project();
        project.setId(4);
        Course course = new Course(42034203, "SE", "Learn common software management", "Teacher Ruan");
        project.setCourse(course);
        project.setProjectName("Test Publish");
        project.setProjectRequire("");
        project.setStartTime(null);
        calendar.set(2022, Calendar.JUNE, 2);
        project.setEndTime(calendar.getTime());
        project.setProjectProportion(10.0D);
        project.setPublisher("Teacher Ruan");
        project.setType(0);

        Mockito.when(projectDAO.save(project)).thenReturn(project);

        Integer publishId = projectService.publish(project);
        System.out.println(publishId);

        Assertions.assertThat(publishId).isEqualTo(4);
    }

    @Test
    void shouldModifyProject() throws Exception {
        Calendar calendar = Calendar.getInstance();

        Project project = new Project();
        project.setId(4);
        Course course = new Course(42034203, "SE", "Learn common software management", "Teacher Ruan");
        project.setCourse(course);
        project.setProjectName("Test Modify");
        project.setProjectRequire("");
        project.setStartTime(null);
        calendar.set(2022, Calendar.JUNE, 2);
        project.setEndTime(calendar.getTime());
        project.setProjectProportion(10.0D);
        project.setPublisher("Teacher Ruan");
        project.setType(0);

        Mockito.when(projectDAO.save(project)).thenReturn(project);

        Integer publishId = projectService.publish(project);
        System.out.println(publishId);

        Assertions.assertThat(publishId).isEqualTo(4);

    }

    @Test
    void shouldGetRemainScore() throws Exception{
        final Integer courseId = 42034203;

        Mockito.when(projectDAO.findAllByCourseId(courseId)).thenReturn(projects);

        Double remain = projectService.remain(courseId);
        System.out.println(remain);

        Assertions.assertThat(remain).isEqualTo(60.0D);
    }
}