package com.karl.chatweb.repository;

import static org.assertj.core.api.Assertions.*;

import com.karl.chatweb.ChatWebApplication;
import com.karl.chatweb.model.Badge;
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
public class UserRepositoryTests {

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
    BadgeRepository badgeRepository;

    @Test
    public void UserRepository_Save_ReturnSavedUser(){
        User user = User.builder()
                .color("#FF0000")
                .role("USER")
                .password("pwd")
                .username("username")
                .badges(new ArrayList<>()).build();

        User savedUser = userRepository.save(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.id).isGreaterThan(0);
    }

    @Test
    public void UserRepository_Save_ReturnSavedUserByUsername(){
        User user = User.builder()
                .color("#FF0000")
                .role("USER")
                .password("pwd")
                .username("username2")
                .badges(new ArrayList<>())
                .build();

        userRepository.save(user);

        User userByUsername = userRepository.findByUsername("username2");

        assertThat(userByUsername).isNotNull();
        assertThat(userByUsername.id).isGreaterThan(0);
    }

    @Test
    public void UserRepository_SaveUserBadges_ReturnUserBadges(){
        Badge badge = Badge.builder().url("http://localhost/test").build();
        Badge badge2 = Badge.builder().url("http://localhost/test2").build();

        ArrayList<Badge> badges = new ArrayList<>();
        badges.add(badgeRepository.save(badge));
        badges.add(badgeRepository.save(badge2));

        User user = User.builder().color("#FF0000").role("USER").password("pwd").username("username3").badges(badges).build();

        User savedUser = userRepository.save(user);

        assertThat(savedUser.badges).isNotEmpty().hasSize(2);

        assertThat(savedUser.badges.getFirst().id).isGreaterThan(0);
        assertThat(savedUser.badges.getLast().id).isGreaterThan(0);
    }
}
