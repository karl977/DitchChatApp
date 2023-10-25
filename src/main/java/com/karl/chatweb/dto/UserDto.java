package com.karl.chatweb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    public String username;
    public String password;
    public String color;
}
