package com.karl.chatweb.repository;

import static org.assertj.core.api.Assertions.*;

import com.karl.chatweb.ChatWebApplication;
import com.karl.chatweb.model.Badge;
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
public class BadgeRepositoryTests {

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
    BadgeRepository badgeRepository;

    @Test
    public void BadgeRepository_Save_ReturnSavedBadge(){
        Badge badge = Badge.builder().url("http://localhost/test2").build();

        Badge savedBadge = badgeRepository.save(badge);

        assertThat(savedBadge).isNotNull();
        assertThat(savedBadge.id).isGreaterThan(0);
    }
}
