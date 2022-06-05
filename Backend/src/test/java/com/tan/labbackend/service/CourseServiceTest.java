package com.tan.labbackend.service;

import com.tan.labbackend.dao.CourseDAO;
import com.tan.labbackend.dao.FeedbackDAO;
import com.tan.labbackend.dao.UserCourseDAO;
import com.tan.labbackend.entity.Course;
import com.tan.labbackend.entity.Project;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

class CourseServiceTest {

    @Mock
    CourseDAO courseDAO;
    @Mock
    UserService userService;
    @Mock
    UserCourseDAO userCourseDAO;
    @Mock
    FeedbackDAO feedbackDAO;

    private CourseService courseService;


    private Course course;

    @BeforeEach
    void setup() {
        courseService = new CourseService(courseDAO,userService,userCourseDAO,feedbackDAO);

        course = new Course(42034203, "SE", "Learn common software management", "Teacher Ruan");
    }
    
    @Test
    @Order(1)
    void shouldGetCourse(){
        final Integer courseId = 42034203;

        Mockito.when(courseDAO.findById(courseId)).thenReturn(Optional.ofNullable(course));

        Course course = courseService.get(courseId);
        System.out.println(course);

        Assertions.assertThat(course.getCourseName()).isEqualTo("SE");
    }
}