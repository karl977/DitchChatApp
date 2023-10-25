package com.karl.chatweb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.karl.chatweb.dto.UserDto;
import com.karl.chatweb.exception.UserAlreadyExistsException;
import com.karl.chatweb.model.User;
import com.karl.chatweb.service.UserService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class UserControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @Test
    public void UserController_RegisterUser_ReturnSuccessTrue() throws Exception {
        UserDto dto = UserDto.builder()
                .username("username")
                .password("password")
                .color("ff0000")
                .build();

        User user = User.builder()
                .color("#FF0000")
                .role("USER")
                .password("password")
                .username("username")
                .badges(new ArrayList<>())
                .build();

        when(userService.registerUser(Mockito.any())).thenReturn(user);

        ResultActions response = mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", CoreMatchers.is(true)));
    }

    @Test
    public void UserController_RegisterUser_ReturnUserAlreadyExists() throws Exception {
        UserDto dto = UserDto.builder()
                .username("username")
                .password("password")
                .color("ff0000")
                .build();

        when(userService.registerUser(Mockito.any())).thenThrow(new UserAlreadyExistsException(""));

        ResultActions response = mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        response.andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", CoreMatchers.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", CoreMatchers.is("User already exists")));
    }
}
