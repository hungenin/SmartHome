package com.homeproject.smarthome.tvguide.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homeproject.smarthome.tvguide.model.Content;
import com.homeproject.smarthome.tvguide.model.dto.ContentDto;
import com.homeproject.smarthome.tvguide.service.ContentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({ContentApiController.class})
public class ContentApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContentService contentService;

    private static final Content TEST_CONTENT = new Content(null, "Test content", "A movie about contents in test.", null);

    @Test
    public void addContent_noContent_returnSameContentDto() throws Exception {
        /*ContentDto contentDto = new ContentDto(TEST_CONTENT);
        when(contentService.add(any())).thenReturn(contentDto);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/contents")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(TEST_CONTENT)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(contentDto.getTitle())));

        verify(contentService, times(1))
                .add(any());*/
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
