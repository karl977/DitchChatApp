package com.karl.chatweb.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = LoginController.class)
@AutoConfigureMockMvc
public class LoginControllerTests {
    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser
    public void LoginController_GetLoggedInWithMockUser_ReturnsOk() throws Exception{
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/user/logged_in"));
        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void LoginController_GetLoggedInWithoutMockUser_Returns401() throws Exception{
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/user/logged_in"));
        response.andExpect(MockMvcResultMatchers.status().is(401));
    }
}
