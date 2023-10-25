package com.karl.chatweb.repository;

import static org.assertj.core.api.Assertions.*;

import com.karl.chatweb.ChatWebApplication;
import com.karl.chatweb.model.Stream;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ChatWebApplication.class)
@Testcontainers
public class StreamRepositoryTests {

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.4");

    @BeforeAll
    public static void beforeClass() {

        postgres.start();
    }

    @AfterAll
    public static void afterClass() {
        postgres.stop();
    }


    @Autowired
    StreamRepository streamRepository;

    @Test
    public void StreamRepository_Save_ReturnSavedStream(){
        Stream stream = Stream.builder()
                .nid("test")
                .name("TestName")
                .avatarUrl("testUrl")
                .category("testCat")
                .videoId("testId")
                .viewers(123L)
                .title("testTitle")
                .build();

        Stream savedStream = streamRepository.save(stream);

        assertThat(savedStream).isNotNull();
        assertThat(savedStream.id).isGreaterThan(0);
    }

    @Test
    public void StreamRepository_Save_ReturnSavedStreamByNid(){

        Stream stream = Stream.builder()
                .nid("test2")
                .name("TestName")
                .avatarUrl("testUrl")
                .category("testCat")
                .videoId("testId")
                .viewers(123L)
                .title("testTitle")
                .build();

        streamRepository.save(stream);

        assertThat(streamRepository.findByNid("test2")).isNotNull();
    }
}
