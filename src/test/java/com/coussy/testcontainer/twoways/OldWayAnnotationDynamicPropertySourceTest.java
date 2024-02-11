package com.coussy.testcontainer.twoways;

import com.coussy.testcontainer.post.Post;
import com.coussy.testcontainer.post.PostRepository;
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

@DataJpaTest(properties = {
        "spring.jpa.hibernate.ddl-auto= create-drop"
})
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OldWayAnnotationDynamicPropertySourceTest {

    @Container
    static GenericContainer database = new PostgreSQLContainer("postgres:12")
            .withDatabaseName("springboot")
            .withPassword("springboot")
            .withUsername("springboot");

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry propertyRegistry) {
        propertyRegistry.add("spring.datasource.url", () -> ((PostgreSQLContainer) database).getJdbcUrl());
        propertyRegistry.add("spring.datasource.username", () -> ((PostgreSQLContainer) database).getUsername());
    }

    @Autowired
    PostRepository postRepository;

    @Test
    void shouldReturnOrdersThatContainMacBookPro() throws InterruptedException {
        Post post = new Post(1,1,"title","body",1);
        postRepository.save(post);
        Optional<Post> byId = postRepository.findById(1);
        Assertions.assertTrue(byId.isPresent());
    }

}
