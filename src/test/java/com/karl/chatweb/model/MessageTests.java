package com.karl.chatweb.model;

import static org.assertj.core.api.Assertions.*;

import com.karl.chatweb.dto.MessageDto;
import com.karl.chatweb.messagepusher.SavedMessage;
import com.karl.chatweb.messagepusher.SavedMessageContent;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

public class MessageTests {

    @Test
    public void Message_CreateMessageAndGetMessageDto_ReturnMessageDto(){
        Badge badge = Badge.builder().url("http://localhost/test").build();
        Badge badge2 = Badge.builder().url("http://localhost/test2").build();

        ArrayList<Badge> badges = new ArrayList<>();
        badges.add(badge);
        badges.add(badge2);

        User user = User.builder()
                .color("#FF0000")
                .role("USER")
                .password("password")
                .username("username")
                .badges(badges).build();

        Stream stream = Stream.builder()
                .nid("test2")
                .name("TestName")
                .avatarUrl("testUrl")
                .category("testCat")
                .videoId("testId")
                .viewers(123L)
                .title("testTitle")
                .build();

        LocalDateTime dateTime = LocalDateTime.now();
        Instant instant = dateTime.toInstant(ZoneOffset.UTC);

        Message message = Message.builder()
                .user(user)
                .stream(stream)
                .text("Text{$}{image}:http://localhost/image{$}Text2")
                .createdOn(instant)
                .build();

        MessageDto dto = message.getMessageDto();

        assertThat(dto.username).isEqualTo("username");
        assertThat(dto.timestamp).isEqualTo(instant.toString());
        assertThat(dto.color).isEqualTo("#FF0000");
        assertThat(dto.badges).hasSize(2);
        assertThat(dto.badges.get(0)).isEqualTo("http://localhost/test");
        assertThat(dto.badges.get(1)).isEqualTo("http://localhost/test2");
        assertThat(dto.messageContents).hasSize(3);
        assertThat(dto.messageContents.get(0).type).isEqualTo("span");
        assertThat(dto.messageContents.get(0).text).isEqualTo("Text");
        assertThat(dto.messageContents.get(1).type).isEqualTo("img");
        assertThat(dto.messageContents.get(1).text).isEqualTo("http://localhost/image");
        assertThat(dto.messageContents.get(2).type).isEqualTo("span");
        assertThat(dto.messageContents.get(2).text).isEqualTo("Text2");
    }

    @Test
    public void Message_SetTextFromSavedMessage_CheckTextProperty(){
        Message message = new Message();
        SavedMessage savedMessage = new SavedMessage();

        SavedMessageContent content1 = new SavedMessageContent();
        content1.type = "text";
        content1.content = "Test1 ";

        SavedMessageContent content2 = new SavedMessageContent();
        content2.type = "image";
        content2.content = "https://localhost/image";

        SavedMessageContent content3 = new SavedMessageContent();
        content3.type = "text";
        content3.content = " Test2";

        ArrayList<SavedMessageContent> contents = new ArrayList<>();
        contents.add(content1);
        contents.add(content2);
        contents.add(content3);

        savedMessage.contents = contents;

        message.setTextFromSavedMessage(savedMessage);

        assertThat(message.text).isEqualTo("Test1 {$}{image}:https://localhost/image{$} Test2");
    }
}
