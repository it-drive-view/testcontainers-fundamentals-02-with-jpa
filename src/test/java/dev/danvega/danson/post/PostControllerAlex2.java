package dev.danvega.danson.post;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.output.ToStringConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.containers.wait.strategy.WaitAllStrategy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

// ici, on considère que le container est prêt dés qu'on reçoit le message dans les logs
//
//

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class PostControllerAlex2 {

    Logger logger = LoggerFactory.getLogger(PostControllerIntTest.class);

    private final ToStringConsumer toStringConsumer = new ToStringConsumer();

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0")
            .waitingFor(          Wait.forLogMessage(".*ready to accept.*", 1));
//            .withExposedPorts(6458);

    @Autowired
    TestRestTemplate restTemplate;

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

}