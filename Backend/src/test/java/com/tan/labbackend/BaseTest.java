package com.tan.labbackend;

import org.testcontainers.containers.MySQLContainer;

public abstract class BaseTest {

    static MySQLContainer mySQLContainer = (MySQLContainer) new MySQLContainer("mysql:latest")
            .withDatabaseName("spring-mysql-db")
            .withUsername("your dockerhub username")
            .withPassword("your dockerhub password")
            .withReuse(true);

    static {
        mySQLContainer.start();
    }
}
