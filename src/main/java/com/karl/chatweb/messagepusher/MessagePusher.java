package com.karl.chatweb.messagepusher;

import com.karl.chatweb.model.Stream;
import com.karl.chatweb.repository.BadgeRepository;
import com.karl.chatweb.repository.MessageRepository;
import com.karl.chatweb.repository.StreamRepository;
import com.karl.chatweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Timer;

@Component
@Profile("!test")
public class MessagePusher {

    @Autowired
    private StreamRepository streamRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BadgeRepository badgeRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @EventListener(ApplicationReadyEvent.class)
    public void afterStartup() {
        List<Stream> streams = streamRepository.findAll();
        for(Stream stream: streams){
            MessagePusherThread thread = new MessagePusherThread(stream.nid, messagingTemplate, badgeRepository, messageRepository, userRepository, streamRepository);
            thread.start();
        }
    }
}
