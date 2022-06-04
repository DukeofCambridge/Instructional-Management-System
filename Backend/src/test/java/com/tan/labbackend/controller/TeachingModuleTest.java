package com.tan.labbackend.controller;
import java.util.Calendar;
import java.util.Date;
import java.time.Instant;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tan.labbackend.entity.Course;

import com.tan.labbackend.entity.Project;
import com.tan.labbackend.service.*;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = ProjectController.class)
public class TeachingModuleTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ProjectService projectService;
    @MockBean
    BoardService boardService;
    @MockBean
    CourseService courseService;
    @MockBean
    UserService userService;
    @MockBean
    ReportService reportService;


    private static List<Project> projects;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void setUp() {

        Calendar calendar = Calendar.getInstance();

        projects = new ArrayList<>();

        Project project = new Project();
        project.setId(1);
        project.setCourse(new Course(42034203,"SE","Learn common software management","Teacher Ruan"));
        project.setProjectName("Process model");
        project.setProjectRequire("null");
        project.setStartTime(null);
        calendar.set(2023, Calendar.JANUARY,1);
        project.setEndTime(calendar.getTime());
        project.setProjectProportion(30.0D);
        project.setPublisher("Teacher Huang");
        project.setType(2);

        projects.add(project);

        Project project1 = new Project();
        project1.setId(2);
        project1.setCourse(new Course(42034203,"SE","Learn common software management","Teacher Ruan"));
        project1.setProjectName("Confrontation exercises");
        project1.setProjectRequire("");
        project1.setStartTime(null);
        calendar.set(2023, Calendar.MAY,1);
        project1.setEndTime(calendar.getTime());
        project1.setProjectProportion(20.0D);
        project1.setPublisher("Teacher Huang");
        project1.setType(0);

        projects.add(project1);

        Project project2 = new Project();
        project2.setId(3);
        project2.setCourse(new Course(42034203,"SE","Learn common software management","Teacher Ruan"));
        project2.setProjectName("Small experiments");
        project2.setProjectRequire("Software engineering prefix knowledge");
        project2.setStartTime(null);
        calendar.set(2022, Calendar.MAY,1);
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
        for(Project p : projects ){
            System.out.println(p);
            if(p.getEndTime().before(new Date())){
                pastProjects.add(p);
            }
        }
        System.out.println(pastProjects);
        Mockito.when(projectService.getPastProjects(courseId)).thenReturn(pastProjects);

        String res = mockMvc.perform(get("/api/projects/42034203/past"))
                .andExpect(jsonPath("$.code", Matchers.is(200)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.object[0].id", Matchers.is(3)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JSONObject jsonRes = JSONObject.parseObject(res);
        System.out.println(jsonRes);
    }

    @Test
    void shouldGetNowProjects() throws Exception {
        final Integer courseId = 42034203;

        List<Project> nowProjects = new ArrayList<>();
        for(Project p : projects ){
            System.out.println(p);
            if(p.getEndTime().after(new Date())){
                nowProjects.add(p);
            }
        }
        System.out.println(nowProjects);
        Mockito.when(projectService.getCurrentProjects(courseId)).thenReturn(nowProjects);

        String res = mockMvc.perform(get("/api/projects/42034203/now"))
                .andExpect(jsonPath("$.code", Matchers.is(200)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.object.size()", Matchers.is(2)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JSONObject jsonRes = JSONObject.parseObject(res);
        System.out.println(jsonRes);
    }

    @Test
    void shouldGetAllProjects() throws Exception{

        final Integer courseId = 42034203;
        Mockito.when(projectService.getAll(courseId)).thenReturn(projects);


        String res = mockMvc.perform(get("/api/projects/42034203/all"))
                .andExpect(jsonPath("$.code", Matchers.is(200)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.object.size()", Matchers.is(3)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JSONObject jsonRes = JSONObject.parseObject(res);
        System.out.println(jsonRes);
    }


    @Test
    void shouldGetProjectDetail() throws Exception{
        final Integer projectId = 2;

        Mockito.when(projectService.getInfo(projectId)).thenReturn(projects.get(1));

        String res = mockMvc.perform(get("/api/projects/2"))
                .andExpect(jsonPath("$.code", Matchers.is(200)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.object.id", Matchers.is(2)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JSONObject jsonRes = JSONObject.parseObject(res);
        System.out.println(jsonRes);
    }

    @Test
    void shouldPublishProject() throws Exception{

        Calendar calendar = Calendar.getInstance();

        Project project = new Project();
        project.setId(4);
        Course course = new Course(42034203, "SE", "Learn common software management", "Teacher Ruan");
        project.setCourse(course);
        project.setProjectName("Test Publish");
        project.setProjectRequire("");
        project.setStartTime(null);
        calendar.set(2022, Calendar.JUNE,2);
        project.setEndTime(calendar.getTime());
        project.setProjectProportion(10.0D);
        project.setPublisher("Teacher Ruan");
        project.setType(0);

        Mockito.when(projectService.publish(project)).thenReturn(project.getId());
        Mockito.when(courseService.get(project.getId())).thenReturn(course);

        String res = mockMvc.perform(post("/api/projects/publish")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(project)))
                .andExpect(jsonPath("$.code", Matchers.is(200)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JSONObject jsonRes = JSONObject.parseObject(res);
        System.out.println(jsonRes);

    }

    @Test
    void shouldModifyProject() throws Exception{

        Calendar calendar = Calendar.getInstance();

        Project project = new Project();
        project.setId(4);
        Course course = new Course(42034203, "SE", "Learn common software management", "Teacher Ruan");
        project.setCourse(course);
        project.setProjectName("Test Modify");
        project.setProjectRequire("");
        project.setStartTime(null);
        calendar.set(2022, Calendar.JUNE,2);
        project.setEndTime(calendar.getTime());
        project.setProjectProportion(10.0D);
        project.setPublisher("Teacher Ruan");
        project.setType(0);

        String res = mockMvc.perform(post("/api/projects/update")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(project)))
                .andExpect(jsonPath("$.code", Matchers.is(200)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JSONObject jsonRes = JSONObject.parseObject(res);
        System.out.println(jsonRes);
    }

    @Test
    void shouldGetRemainScore() throws Exception{
        final Double remainScore = 40.0D;
        final Integer courseId = 42034203;

        Mockito.when(projectService.remain(courseId)).thenReturn(remainScore);

        String res = mockMvc.perform(get("/api/projects/42034203/remain"))
                .andExpect(jsonPath("$.code", Matchers.is(200)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JSONObject jsonRes = JSONObject.parseObject(res);
        System.out.println(jsonRes);

    }

}