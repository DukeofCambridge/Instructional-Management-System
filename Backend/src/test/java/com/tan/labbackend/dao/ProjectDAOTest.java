package com.tan.labbackend.dao;

import com.tan.labbackend.BaseTest;
import com.tan.labbackend.entity.Course;
import com.tan.labbackend.entity.Project;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class ProjectDAOTest extends BaseTest {

    @Autowired
    private ProjectDAO projectDAO;


    @Test
    @Order(1)
    public void shouldSaveProject() {

        Calendar calendar = Calendar.getInstance();

        Project project = new Project();
        Course course = new Course(42034203, "SE", "Learn common software management", "Teacher Ruan");
        project.setCourse(course);
        project.setProjectName("Test Save");
        project.setProjectRequire("");
        project.setStartTime(null);
        calendar.set(2022, Calendar.JUNE, 2);
        project.setEndTime(calendar.getTime());
        project.setProjectProportion(10.0D);
        project.setPublisher("Teacher Ruan");
        project.setType(0);

        Project savedProject = projectDAO.save(project);

        System.out.println(savedProject);

        assertThat(savedProject).usingRecursiveComparison()
                .ignoringFields("id").isEqualTo(project);
    }

}