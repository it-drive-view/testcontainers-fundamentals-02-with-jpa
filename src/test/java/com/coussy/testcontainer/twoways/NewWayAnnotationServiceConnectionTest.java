package com.coussy.testcontainer.twoways;

import com.coussy.testcontainer.post.Post;
import com.coussy.testcontainer.post.PostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
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
public class NewWayAnnotationServiceConnectionTest {

    @ServiceConnection
    @Container
    static GenericContainer database = new PostgreSQLContainer("postgres:12")
            .withDatabaseName("springboot")
            .withPassword("springboot")
            .withUsername("springboot");

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
