package dev.danvega.danson.post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class PostControllerIntTest {

    Logger logger = LoggerFactory.getLogger(PostControllerIntTest.class);

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");

    @Autowired
    TestRestTemplate restTemplate;


    /* système de log 1
     */
    @BeforeEach
    void followContainerLOgs1() throws InterruptedException {
        postgres.followOutput(new Slf4jLogConsumer(LoggerFactory.getLogger("docker logs")));
        Thread.sleep(1000 * 1000);
    }


    @Test
    void connectionEstablished() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    void shouldFindAllPosts() {
        Post[] posts = restTemplate.getForObject("/api/posts", Post[].class);
        assertThat(posts.length).isGreaterThan(100);
    }
    
    @Test
    void shouldFindPostWhenValidPostID() {
        ResponseEntity<Post> response = restTemplate.exchange("/api/posts/1", HttpMethod.GET, null, Post.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void shouldThrowNotFoundWhenInvalidPostID() {
        ResponseEntity<Post> response = restTemplate.exchange("/api/posts/999", HttpMethod.GET, null, Post.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @Rollback
    void shouldCreateNewPostWhenPostIsValid() {
        Post post = new Post(101,1,"101 Title","101 Body",null);

        ResponseEntity<Post> response = restTemplate.exchange("/api/posts", HttpMethod.POST, new HttpEntity<Post>(post), Post.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(Objects.requireNonNull(response.getBody()).getId()).isEqualTo(101);
        assertThat(response.getBody().getUserId()).isEqualTo(1);
        assertThat(response.getBody().getTitle()).isEqualTo("101 Title");
        assertThat(response.getBody().getBody()).isEqualTo("101 Body");
    }

    @Test
    void shouldNotCreateNewPostWhenValidationFails() {
        Post post = new Post(101,1,"","",null);
        ResponseEntity<Post> response = restTemplate.exchange("/api/posts", HttpMethod.POST, new HttpEntity<Post>(post), Post.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @Rollback
    void shouldUpdatePostWhenPostIsValid() {
        ResponseEntity<Post> response = restTemplate.exchange("/api/posts/99", HttpMethod.GET, null, Post.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Post existing = response.getBody();
        assertThat(existing).isNotNull();
        Post updated = new Post(existing.getId(),existing.getUserId(),"NEW POST TITLE #1", "NEW POST BODY #1",existing.getVersion());

        assertThat(updated.getId()).isEqualTo(99);
        assertThat(updated.getUserId()).isEqualTo(10);
        assertThat(updated.getTitle()).isEqualTo("NEW POST TITLE #1");
        assertThat(updated.getBody()).isEqualTo("NEW POST BODY #1");
    }

    @Test
    @Rollback
    void shouldDeleteWithValidID() {
        ResponseEntity<Void> response = restTemplate.exchange("/api/posts/88", HttpMethod.DELETE, null, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

}