package com.tan.labbackend.dao;

import com.tan.labbackend.BaseTest;
import com.tan.labbackend.entity.Course;
import com.tan.labbackend.entity.Project;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Calendar;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class CourseDAOTest extends BaseTest {

    @Autowired
    private CourseDAO courseDAO;

    @Test
    @Order(1)
    public void shouldSaveCourse() {

        Course course = new Course(42034203, "SE", "Learn common software management", "Teacher Ruan");

        Course savedCourse = courseDAO.save(course);

        System.out.println(savedCourse);

        assertThat(savedCourse).usingRecursiveComparison()
                .ignoringFields("id").isEqualTo(course);

    }

}