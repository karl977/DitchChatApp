package com.karl.chatweb.model;

import com.karl.chatweb.dto.MessageContentDto;
import com.karl.chatweb.dto.MessageDto;
import com.karl.chatweb.messagepusher.SavedMessage;
import com.karl.chatweb.messagepusher.SavedMessageContent;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Objects;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "messages", indexes = {
        @Index(name = "idx_messages_user_id", columnList = "user_id"),
        @Index(name = "idx_messages_stream_id", columnList = "stream_id")})
public class Message {
    @Id
    @GeneratedValue
    public Long id;

    @Column(columnDefinition = "TEXT")
    public String text;

    @CreationTimestamp
    public Instant createdOn;

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User user;

    @ManyToOne
    @JoinColumn(name = "stream_id")
    public Stream stream;

    public MessageDto getMessageDto() {

        ArrayList<MessageContentDto> contents  = new ArrayList<>();
        ArrayList<String> badges = new ArrayList<>();

        for (Badge badge : this.user.badges) {
            badges.add(badge.url);
        }

        String[] contentParts = this.text.split("\\{\\$\\}");
        for (String part : contentParts) {
            MessageContentDto mcDto = new MessageContentDto();
            if (part.startsWith("{image}:")) {
                mcDto.type = "img";
                mcDto.text = part.substring(8);
            } else {
                mcDto.type = "span";
                mcDto.text = part;
            }
            contents.add(mcDto);
        }
        return MessageDto.builder()
                .id(this.id)
                .color(this.user.color)
                .username(this.user.username)
                .timestamp(this.createdOn.toString())
                .badges(badges)
                .messageContents(contents)
                .build();
    }

    public void setTextFromSavedMessage(SavedMessage savedMessage){
        StringBuilder res = new StringBuilder();
        for(SavedMessageContent content: savedMessage.contents){
            if(Objects.equals(content.type, "text")){
                res.append("{$}");
                res.append(content.content);
            }else if(Objects.equals(content.type, "image")){
                res.append("{$}{image}:");
                res.append(content.content);
            }
        }

        if(res.length() >= 3){
            this.text = res.substring(3);
        }else{
            this.text = res.toString();
        }
    }
}
