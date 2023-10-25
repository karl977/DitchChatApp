package com.karl.chatweb.controller;

import static org.hamcrest.Matchers.hasSize;

import com.karl.chatweb.model.Stream;
import com.karl.chatweb.repository.StreamRepository;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;

import static org.mockito.Mockito.when;

@WebMvcTest(controllers = StreamController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class StreamControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    StreamRepository streamRepository;

    @Test
    public void StreamController_GetStream_ReturnStream() throws Exception {
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
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nid", CoreMatchers.is(stream.nid)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(stream.name)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.avatar_url", CoreMatchers.is(stream.avatarUrl)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.category", CoreMatchers.is(stream.category)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.video_id", CoreMatchers.is(stream.videoId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.viewers", CoreMatchers.is(stream.viewers.intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.is(stream.title)));
    }

    @Test
    public void StreamController_GetStream_Return404() throws Exception {
        when(streamRepository.findByNid("teststream")).thenReturn(null);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/user/streams/teststream"));

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
    }
    @Test
    public void StreamController_GetStreams_ReturnStreams() throws Exception {
        ArrayList<Stream> streams = new ArrayList<>();

        Stream stream = Stream.builder()
                .nid("test")
                .name("TestName")
                .avatarUrl("testUrl")
                .category("testCat")
                .videoId("testId")
                .viewers(123L)
                .title("testTitle")
                .build();

        Stream stream2 = Stream.builder()
                .nid("test2")
                .name("TestName2")
                .avatarUrl("testUrl2")
                .category("testCat2")
                .videoId("testId2")
                .viewers(1232L)
                .title("testTitle2")
                .build();


        streams.add(stream);
        streams.add(stream2);
        when(streamRepository.findAll()).thenReturn(streams);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/user/streams"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)));
    }
}
