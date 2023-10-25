package com.karl.chatweb.websocket;

import com.karl.chatweb.dto.ClientMessageDto;
import com.karl.chatweb.model.Message;
import com.karl.chatweb.model.User;
import com.karl.chatweb.model.Stream;
import com.karl.chatweb.repository.MessageRepository;
import com.karl.chatweb.repository.StreamRepository;
import com.karl.chatweb.repository.UserRepository;
import com.karl.chatweb.dto.MessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class MessagingController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private StreamRepository streamRepository;


    @MessageMapping("/stream/{nid}")
    @SendTo("/topic/stream/{nid}")
    public MessageDto message(@DestinationVariable String nid, ClientMessageDto clientMessage, SimpMessageHeaderAccessor headerAccessor) throws Exception {
        if(headerAccessor.getUser() != null && headerAccessor.getUser().getName() != null){
            String username = headerAccessor.getUser().getName();
            User user = userRepository.findByUsername(username);

            Stream stream = streamRepository.findByNid(nid);

            Message message = Message.builder()
                    .text(clientMessage.text)
                    .user(user)
                    .stream(stream)
                    .build();

            messageRepository.save(message);

            return message.getMessageDto();
        }else{
            return null;
        }
    }
}
