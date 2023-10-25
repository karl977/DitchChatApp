package com.karl.chatweb.repository;

import static org.assertj.core.api.Assertions.*;

import com.karl.chatweb.ChatWebApplication;
import com.karl.chatweb.model.Message;
import com.karl.chatweb.model.Stream;
import com.karl.chatweb.model.User;
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

import java.util.ArrayList;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ChatWebApplication.class)
@Testcontainers
public class MessageRepositoryTests {

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
    UserRepository userRepository;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    StreamRepository streamRepository;

    @Test
    public void MessageRepository_Save_ReturnSavedMessage(){

        User user = User.builder()
                .color("#FF0000")
                .role("USER")
                .password("pwd")
                .username("username3")
                .badges(new ArrayList<>()).build();

        user = userRepository.save(user);

        Stream stream = Stream.builder()
                .nid("test2")
                .name("TestName")
                .avatarUrl("testUrl")
                .category("testCat")
                .videoId("testId")
                .viewers(123L)
                .title("testTitle")
                .build();

        stream = streamRepository.save(stream);

        Message message = Message.builder().user(user).stream(stream).text("Test").build();


        Message savedMessage = messageRepository.save(message);


        assertThat(savedMessage).isNotNull();
        assertThat(savedMessage.id).isGreaterThan(0);
    }
}
