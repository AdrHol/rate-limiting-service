package com.adrhol.rate_limiting_service.filter;

import com.adrhol.rate_limiting_service.model.RateBucketDTO;
import com.adrhol.rate_limiting_service.service.RatesBucketService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static reactor.core.publisher.Mono.when;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class RedirectFilterTest {

    @Autowired
    private MockMvc mockMvc;
    @Value("${redirect.destination}")
    private String DESTINATION;
    @InjectMocks
    private RatesBucketService ratesBucketService;
    @MockBean
    private RedisTemplate<String, RateBucketDTO> redisTemplate;
    @MockBean
    private HashOperations hashOperations;
    private final String REQUESTED_URL = "/TEST";

    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks(this);
        RateBucketDTO redisResponseMock = new RateBucketDTO("test", 5, LocalDateTime.now().plus(Duration.ofSeconds(30)));
        Mockito.when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        Mockito.when(hashOperations.get(anyString(), anyString())).thenReturn(redisResponseMock);

        mockMvc = MockMvcBuilders.standaloneSetup()
                .addFilters(new RedirectFilter(ratesBucketService))
                .build();
    }

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