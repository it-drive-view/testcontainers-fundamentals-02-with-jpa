package dev.danvega.danson.post.schemacreation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.output.ToStringConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

// lancer le test dans un terminal selon :
// mvn -Dtest=PostControllerAlex6"
//
// on a besoin absolument de @ServiceConnection
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class NewAnnotation {

    @Container
    @ServiceConnection
    public static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16.0");

    private final ToStringConsumer toStringConsumer = new ToStringConsumer();

    @BeforeEach
    void followContainerLOgs1() throws InterruptedException {
        postgres.followOutput(new Slf4jLogConsumer(LoggerFactory.getLogger("docker logs postgres 1")));
    }

    @Test
    void connectionEstablished() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

}
