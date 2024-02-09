package dev.danvega.danson.post.crud;

import dev.danvega.danson.post.Post;
import dev.danvega.danson.post.PostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
// 1- si je mets cette annotation, alors
//@SpringBootTest
// 2- si je mets cell-ci, tout est OK
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// TODO pourquoi cette annotation ci-dessous.. ?
// @Transactional
class CrudTest2 {

    @Autowired
    PostRepository postRepository;

    Logger logger = LoggerFactory.getLogger(CrudTest2.class);

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");

    /* système de log 1
     */
    @BeforeEach
    void followContainerLOgs1() throws InterruptedException {
        postgres.followOutput(new Slf4jLogConsumer(LoggerFactory.getLogger("docker logs")));
    }

    @Test
    void connectionEstablished() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

    @BeforeEach
    void setUp() {
        List<Post> posts = List.of(new Post(1,1,"Hello, World!", "This is my first post!",null));
        postRepository.saveAll(posts);
    }

    @Test
    void shouldReturnPostByTitle() {
//        Post post = postRepository.findByTitle("Hello, World!").orElseThrow();
//        assertEquals("Hello, World!", post.title(), "Post title should be 'Hello, World!'");
    }

    @Test
    void shouldFindAllPosts() {
//        Post post = new Post(1,1,"title","body",1);
//        postRepository.save(post);
//        Optional<Post> optional = postRepository.findById(1);
//        Assertions.assertTrue(optional.isPresent());
    }

}