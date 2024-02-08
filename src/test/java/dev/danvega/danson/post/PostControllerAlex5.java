package dev.danvega.danson.post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.output.ToStringConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

// lancer le test dans un terminal selon :
// mvn -Dtest=PostControllerAlex5 test | grep -E "org.testcontainers.containers.output.WaitingConsumer|Could"
// OU BIEN
// mvn -Dtest=PostControllerAlex5 test | grep -E "org.testcontainers|Could"
// ICI :
// 1- je créé un container que je fais exprès de planter : le port 5454 ci-dessous ne peut pas être mappé
// 2- on va bien jusqu'à la création du conteneur
// 3- en mode terminal, le container aurait démarré. Mais, ici Testcontainers s'assure que le port puisse être contacté de l'extérieur


@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class PostControllerAlex5 {

    Logger logger = LoggerFactory.getLogger(PostControllerIntTest.class);

    private final ToStringConsumer toStringConsumer = new ToStringConsumer();

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0")
            .withExposedPorts(5454);

    @Autowired
    TestRestTemplate restTemplate;

    /* système de log 1
     */
    @BeforeEach
    void followContainerLOgs1() throws InterruptedException {
        postgres.followOutput(new Slf4jLogConsumer(LoggerFactory.getLogger("docker logs postgres 1")));
    }

    @Test
    void connectionEstablished() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

//    @Test
//    void connectionEstablished2() {
//        assertThat(postgres2.isCreated()).isTrue();
//        assertThat(postgres2.isRunning()).isTrue();
//    }


}


//    @Container
//    @ServiceConnection
//    private static GenericContainer postgres = new GenericContainer(DockerImageName.parse("postgres:12.12"))
//            .withEnv("POSTGRES_USER", "postgres")
//            .withEnv("POSTGRES_PASSWORD", "postgres")
//            .withEnv("POSTGRES_DB", "tasklist")
//            .withExposedPorts(5432);

//            .withClasspathResourceMapping("Postgresinit.sql", "/docker-entrypoint-initdb.d/script.sql", BindMode.READ_WRITE)
