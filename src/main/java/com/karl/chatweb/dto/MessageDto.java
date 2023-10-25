package com.karl.chatweb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    public Long id;
    public String timestamp;
    public ArrayList<String> badges;
    public String username;
    public String color;

    public ArrayList<MessageContentDto> messageContents;
}