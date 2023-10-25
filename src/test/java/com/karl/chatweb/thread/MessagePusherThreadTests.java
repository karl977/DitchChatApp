package com.karl.chatweb.thread;

import com.karl.chatweb.dto.MessageDto;
import com.karl.chatweb.messagepusher.MessagePusherThread;
import com.karl.chatweb.model.Message;
import com.karl.chatweb.model.Stream;
import com.karl.chatweb.model.User;
import com.karl.chatweb.repository.BadgeRepository;
import com.karl.chatweb.repository.MessageRepository;
import com.karl.chatweb.repository.StreamRepository;
import com.karl.chatweb.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MessagePusherThreadTests {
    @Mock
    SimpMessageSendingOperations messagingTemplate;

    @Mock
    BadgeRepository badgeRepository;

    @Mock
    MessageRepository messageRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    StreamRepository streamRepository;

    @Test
    public void MessagePusherThread_StartThread_AssertDbEntitiesCreatedAndMessagesSent() throws InterruptedException {
        MessagePusherThread thread = new MessagePusherThread("test", messagingTemplate, badgeRepository, messageRepository, userRepository, streamRepository);

        Stream stream = Stream.builder()
                .nid("test")
                .name("Test")
                .avatarUrl("testUrl")
                .category("testCat")
                .videoId("testId")
                .viewers(123L)
                .title("testTitle")
                .build();

        when(streamRepository.findByNid(anyString())).thenReturn(stream);

        User user = User.builder()
                .color("#FF0000")
                .role("USER")
                .password("password")
                .username("username")
                .badges(new ArrayList<>())
                .build();

        when(userRepository.findByUsername(anyString())).thenReturn(user);
        when(userRepository.findByUsername("Vanidor")).thenReturn(null);
        when(userRepository.findByUsername("djabski_")).thenReturn(null);
        when(userRepository.findByUsername("testuser")).thenReturn(null);

        Message message = Message.builder()
                .id(1L)
                .text("text")
                .user(user)
                .stream(stream)
                .createdOn(LocalDateTime.now().toInstant(ZoneOffset.UTC))
                .build();

        when(messageRepository.save(any())).thenReturn(message);

        thread.start();
        Thread.sleep(2000);
        try{
            thread.interrupt();
        }catch(Exception ignored){}

        verify(messagingTemplate, times(2)).convertAndSend(anyString(), any(MessageDto.class));
        verify(badgeRepository, times(3)).save(any());
        verify(userRepository, times(3)).save(any());
    }
}
