package com.adrhol.rate_limiting_service.filter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RedirectFilterTest {

    @Autowired
    private MockMvc mockMvc;
    @Value("${redirect.destination}")
    private String DESTINATION;
    private final String REQUESTED_URL = "/TEST";

    @Test
    void shouldReturnStatusRedirectForGetRequestWhenDoFilter() throws Exception {
//        given
        String expectedDestination = DESTINATION + REQUESTED_URL;
//        when / then
        mockMvc.perform(MockMvcRequestBuilders.get(REQUESTED_URL))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(expectedDestination));
    }
    @Test
    void shouldReturnStatusRedirectForPostRequestWhenDoFilter() throws Exception {
//        given
        String expectedDestination = DESTINATION + REQUESTED_URL;
//        when / then
        mockMvc.perform(MockMvcRequestBuilders.post(REQUESTED_URL))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(expectedDestination));
    }
    @Test
    void shouldReturnStatusRedirectForPutRequestWhenDoFilter() throws Exception {
//        given
        String expectedDestination = DESTINATION + REQUESTED_URL;
//        when / then
        mockMvc.perform(MockMvcRequestBuilders.put(REQUESTED_URL))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(expectedDestination));
    }
    @Test
    void shouldReturnStatusRedirectForDeleteRequestWhenDoFilter() throws Exception {
//        given
        String expectedDestination = DESTINATION + REQUESTED_URL;
//        when / then
        mockMvc.perform(MockMvcRequestBuilders.delete(REQUESTED_URL))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(expectedDestination));
    }
}