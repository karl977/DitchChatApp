package com.karl.chatweb.messagepusher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.karl.chatweb.model.Badge;
import com.karl.chatweb.model.Message;
import com.karl.chatweb.model.Stream;
import com.karl.chatweb.model.User;
import com.karl.chatweb.repository.BadgeRepository;
import com.karl.chatweb.repository.MessageRepository;
import com.karl.chatweb.repository.StreamRepository;
import com.karl.chatweb.repository.UserRepository;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessagePusherThread extends Thread {

    public String streamNid;
    private final SimpMessageSendingOperations messagingTemplate;

    private final BadgeRepository badgeRepository;

    private final MessageRepository messageRepository;

    private final UserRepository userRepository;

    private final StreamRepository streamRepository;

    public MessagePusherThread(String streamNid, SimpMessageSendingOperations messagingTemplate, BadgeRepository badgeRepository, MessageRepository messageRepository, UserRepository userRepository, StreamRepository streamRepository) {
        this.streamNid = streamNid;
        this.messagingTemplate = messagingTemplate;
        this.badgeRepository = badgeRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.streamRepository = streamRepository;
    }

    @Override
    public void run() {

        try {
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("messages/" + streamNid + ".json");
            ObjectMapper mapper = new ObjectMapper();
            List<SavedMessage> messages = Arrays.asList(mapper.readValue(inputStream, SavedMessage[].class));

            Stream stream = streamRepository.findByNid(streamNid);
            if(stream == null){
                throw new RuntimeException("Stream with nid '" + streamNid + "'  not found");
            }

            // Create users and badges from all messages
            createUsersAndBadges(messages);

            int currentTime =  messages.get(0).time;

            for(int i = 0; i < messages.size(); i++){
                SavedMessage message = messages.get(i);
                int delta = message.time - currentTime;
                if(delta > 0){
                    if(delta > 5){
                        Thread.sleep(5000);
                    }else{
                        Thread.sleep(delta * 1000);
                    }
                }

                Message newMessage = Message.builder()
                        .stream(stream)
                        .user(userRepository.findByUsername(message.username))
                        .build();
                newMessage.setTextFromSavedMessage(message);

                newMessage = messageRepository.save(newMessage);

                messagingTemplate.convertAndSend("/topic/stream/" + this.streamNid, newMessage.getMessageDto());

                currentTime = message.time;

                if(i == messages.size() - 1){
                    i = 0;
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }catch (InterruptedException e){

        }
    }

    private void createUsersAndBadges(List<SavedMessage> messages){
        for (SavedMessage message : messages) {
            User user = userRepository.findByUsername(message.username);
            if (user == null) {
                ArrayList<Badge> badges = new ArrayList<>();
                for(String badgeUrl : message.badges){
                    Badge badge = badgeRepository.findByUrl(badgeUrl);
                    if(badge == null){
                        badge = Badge.builder()
                                .url(badgeUrl)
                                .build();

                        badgeRepository.save(badge);
                    }
                    badges.add(badge);
                }

                // Builder here threw hibernate exception about lazy loading of badges
                User newUser = new User();
                newUser.username = message.username;
                newUser.password = "$2a$10$sq6V2ktvA50AyDA3QU4YGOli.YhdqivMcwSHyv674K9e8197eyPKG";
                newUser.color = message.color;
                newUser.role = "USER";
                newUser.badges = badges;


                userRepository.save(newUser);
            }


        }
    }
}
