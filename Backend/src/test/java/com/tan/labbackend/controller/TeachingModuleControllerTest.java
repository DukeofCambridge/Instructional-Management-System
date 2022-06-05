package com.tan.labbackend.controller;
import com.tan.labbackend.entity.Role;
import com.tan.labbackend.entity.User;
import java.util.Calendar;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tan.labbackend.entity.Course;

import com.tan.labbackend.entity.Project;
import com.tan.labbackend.entity.Report;
import com.tan.labbackend.service.*;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TeachingModuleControllerTest {
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

    private static Report report;

    private static User user;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void setUp() {

        Calendar calendar = Calendar.getInstance();

        projects = new ArrayList<>();

        Project project1 = new Project();
        project1.setId(1);
        project1.setCourse(new Course(42034203,"SE","Learn common software management","Teacher Ruan"));
        project1.setProjectName("Process model");
        project1.setProjectRequire("null");
        project1.setStartTime(null);
        calendar.set(2023, Calendar.JANUARY,1);
        project1.setEndTime(calendar.getTime());
        project1.setProjectProportion(30.0D);
        project1.setPublisher("Teacher Huang");
        project1.setType(2);

        projects.add(project1);

        Project project2 = new Project();
        project2.setId(2);
        project2.setCourse(new Course(42034203,"SE","Learn common software management","Teacher Ruan"));
        project2.setProjectName("Confrontation exercises");
        project2.setProjectRequire("");
        project2.setStartTime(null);
        calendar.set(2023, Calendar.MAY,1);
        project2.setEndTime(calendar.getTime());
        project2.setProjectProportion(20.0D);
        project2.setPublisher("Teacher Huang");
        project2.setType(0);

        projects.add(project2);

        Project project3 = new Project();
        project3.setId(3);
        project3.setCourse(new Course(42034203,"SE","Learn common software management","Teacher Ruan"));
        project3.setProjectName("Small experiments");
        project3.setProjectRequire("Software engineering prefix knowledge");
        project3.setStartTime(null);
        calendar.set(2022, Calendar.MAY,1);
        project3.setEndTime(calendar.getTime());
        project3.setProjectProportion(10.0D);
        project3.setPublisher("Teacher Ruan");
        project3.setType(0);

        projects.add(project3);

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

    @Test()
    @Order(1)
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
    @Order(2)
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
    @Order(3)
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
    @Order(4)
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
    @Order(12)
    void shouldNotGetProjectDetail() throws Exception{
        final Integer projectId = 7;

        Mockito.when(projectService.getInfo(projectId)).thenReturn(null);

        String res = mockMvc.perform(get("/api/projects/7"))
                .andExpect(jsonPath("$.code", Matchers.is(400)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JSONObject jsonRes = JSONObject.parseObject(res);
        System.out.println(jsonRes);
    }

    @Test
    @Order(5)
    void shouldPublishProject() throws Exception{

        Calendar calendar = Calendar.getInstance();

        Project project4 = new Project();
        project4.setId(4);
        Course course = new Course(42034203, "SE", "Learn common software management", "Teacher Ruan");
        project4.setCourse(course);
        project4.setProjectName("Test Publish");
        project4.setProjectRequire("");
        project4.setStartTime(null);
        calendar.set(2022, Calendar.JUNE,2);
        project4.setEndTime(calendar.getTime());
        project4.setProjectProportion(10.0D);
        project4.setPublisher("Teacher Ruan");
        project4.setType(0);

        Mockito.when(projectService.publish(project4)).thenReturn(project4.getId());
        Mockito.when(courseService.get(project4.getId())).thenReturn(course);

        String res = mockMvc.perform(post("/api/projects/publish")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(project4)))
                .andExpect(jsonPath("$.code", Matchers.is(200)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JSONObject jsonRes = JSONObject.parseObject(res);
        System.out.println(jsonRes);
    }


    @Test
    @Order(6)
    void shouldModifyProject() throws Exception{

        Calendar calendar = Calendar.getInstance();

        Project project5 = new Project();
        project5.setId(4);
        Course course = new Course(42034203, "SE", "Learn common software management", "Teacher Ruan");
        project5.setCourse(course);
        project5.setProjectName("Test Modify");
        project5.setProjectRequire("");
        project5.setStartTime(null);
        calendar.set(2022, Calendar.JUNE,2);
        project5.setEndTime(calendar.getTime());
        project5.setProjectProportion(10.0D);
        project5.setPublisher("Teacher Ruan");
        project5.setType(0);

        String res = mockMvc.perform(post("/api/projects/update")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(project5)))
                .andExpect(jsonPath("$.code", Matchers.is(200)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JSONObject jsonRes = JSONObject.parseObject(res);
        System.out.println(jsonRes);
    }

    @Test
    @Order(7)
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

    @Test
    @Order(8)
    void shouldGetReportTemplate() throws Exception {


        Mockito.when(reportService.get(0)).thenReturn(report);

        String res = mockMvc.perform(get("/api/projects/template"))
                .andExpect(jsonPath("$.code", Matchers.is(200)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.object.id", Matchers.is(1)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JSONObject jsonRes = JSONObject.parseObject(res);
        System.out.println(jsonRes);
    }

    @Test
    @Order(9)
    void shouldSaveReport() throws Exception {

        String res = mockMvc.perform(post("/api/projects/save")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(report)))
                .andExpect(jsonPath("$.code", Matchers.is(200)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JSONObject jsonRes = JSONObject.parseObject(res);
        System.out.println(jsonRes);
    }

    @Test
    @Order(10)
    void shouldGetReport() throws Exception {

        final Integer projectId = 1;
        final String username = "1952111";

        Mockito.when(projectService.get(projectId)).thenReturn(projects.get(0));
        Mockito.when(userService.findByUsername(username)).thenReturn(user);
        Mockito.when(reportService.getReport(projects.get(0),user)).thenReturn(report);



        String res = mockMvc.perform(get("/api/projects/getReport")
                        .param("projectId", String.valueOf(1))
                        .param("username","1952111"))
                .andExpect(jsonPath("$.code", Matchers.is(200)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.object.id", Matchers.is(1)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JSONObject jsonRes = JSONObject.parseObject(res);
        System.out.println(jsonRes);

    }


    @Test
    @Order(11)
    void shouldGet400BecauseReportNotExist() throws Exception {

        final Integer projectId = 1;
        final String username = "1952111";

        Mockito.when(projectService.get(projectId)).thenReturn(projects.get(0));
        Mockito.when(userService.findByUsername(username)).thenReturn(user);
        Mockito.when(reportService.getReport(projects.get(0),user)).thenReturn(null);



        String res = mockMvc.perform(get("/api/projects/getReport")
                        .param("projectId", String.valueOf(1))
                        .param("username","1952111"))
                .andExpect(jsonPath("$.code", Matchers.is(400)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.msg", Matchers.is("学生未提交作业")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JSONObject jsonRes = JSONObject.parseObject(res);
        System.out.println(jsonRes);

    }


}