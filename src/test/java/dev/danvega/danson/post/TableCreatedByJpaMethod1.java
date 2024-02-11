package dev.danvega.danson.post;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

// 1- we need to create "post" table in the database
// 2- we are in jpa/hibernate, so with @Entity annotation, program will be able to create the table
// 3- but we have to specify the property : "spring.jpa.hibernate.ddl-auto= create-drop"
// 4- this will be done into the annotation @DataJpaTest below

@DataJpaTest(properties = {
        "spring.jpa.hibernate.ddl-auto= create-drop"
})
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TableCreatedByJpaMethod1 {

    @Container
    static GenericContainer database = new PostgreSQLContainer("postgres:12")
            .withDatabaseName("springboot")
            .withPassword("springboot")
            .withUsername("springboot");

    @Autowired
    PostRepository postRepository;

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry propertyRegistry) {
        propertyRegistry.add("spring.datasource.url", () -> ((PostgreSQLContainer) database).getJdbcUrl());
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
