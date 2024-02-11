package com.coussy.testcontainer;

import com.coussy.testcontainer.post.Post;
import com.coussy.testcontainer.post.PostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

// 1- we need to create "post" table in the database
// 2- we are in jpa/hibernate, so with @Entity annotation, program SHOULD BE ABLE to create the table
// 3- BUT : the property in the properties file is "spring.jpa.hibernate.ddl-auto= validate" so the table will not be created (validate is not sufficent)
// 4- the "post" table will be created at the creation of the postgres container, with the help of the PostgresInit.sql

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TableCreatedByDockerScriptTest {

    @Container
    static GenericContainer database = new PostgreSQLContainer("postgres:12")
            .withDatabaseName("springboot")
            .withPassword("springboot")
            .withUsername("springboot")
            .withClasspathResourceMapping("PostgresInit.sql", "/docker-entrypoint-initdb.d/script.sql", BindMode.READ_ONLY);

    @Autowired
    PostRepository postRepository;

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry propertyRegistry) {
        propertyRegistry.add("spring.datasource.url", () -> ((PostgreSQLContainer) database).getJdbcUrl());
        System.out.println("===> " +  ((PostgreSQLContainer) database).getJdbcUrl());
        propertyRegistry.add("spring.datasource.username", () -> ((PostgreSQLContainer) database).getUsername());
    }

    @Test
    void shouldReturnOrdersThatContainMacBookPro() throws InterruptedException {
        Post post = new Post(1,1,"title","body",1);
        postRepository.save(post);
        Optional<Post> byId = postRepository.findById(1);
        Assertions.assertTrue(byId.isPresent());
    }

}
