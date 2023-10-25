package com.karl.chatweb.security;


import static org.assertj.core.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.karl.chatweb.config.SecurityConfig;
import com.karl.chatweb.controller.StreamController;
import com.karl.chatweb.controller.UserController;
import com.karl.chatweb.dto.UserDto;
import com.karl.chatweb.model.Stream;
import com.karl.chatweb.model.User;
import com.karl.chatweb.repository.StreamRepository;
import com.karl.chatweb.service.UserService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest({UserController.class, StreamController.class})
@ExtendWith(MockitoExtension.class)
@Import(SecurityConfig.class)
public class ControllerAuthorizationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    StreamRepository streamRepository;

    @MockBean
    UserService userService;


    @Test
    public void UserController_RegisterWithoutUser_ReturnSuccess() throws Exception{
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
                .with(CsrfRequestPostProcessor.csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", CoreMatchers.is(true)));
    }

    @Test
    public void UserController_RegisterWithoutCsrf_Return403() throws Exception{
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
                //.with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        response.andExpect(MockMvcResultMatchers.status().is(403));
    }

    @Test
    @WithMockUser
    public void StreamController_GetStreamWithAuthenticatedUser_Return200() throws Exception{
        Stream stream = Stream.builder()
                .nid("teststream")
                .name("TestName")
                .avatarUrl("testUrl")
                .category("testCat")
                .videoId("testId")
                .viewers(123L)
                .title("testTitle")
                .build();

        when(streamRepository.findByNid("teststream")).thenReturn(stream);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/user/streams/teststream"));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void StreamController_GetStreamWithNoAuthenticatedUser_RedirectToNotLoggedInPage() throws Exception{
        Stream stream = Stream.builder()
                .nid("teststream")
                .name("TestName")
                .avatarUrl("testUrl")
                .category("testCat")
                .videoId("testId")
                .viewers(123L)
                .title("testTitle")
                .build();

        when(streamRepository.findByNid("teststream")).thenReturn(stream);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/user/streams/teststream"));

        response.andExpect(MockMvcResultMatchers.status().is(302));
        String location = response.andReturn().getResponse().getHeader("Location");
        assertThat(location).contains("/api/not_logged_in");
    }
}
