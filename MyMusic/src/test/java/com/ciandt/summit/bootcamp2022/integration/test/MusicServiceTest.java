package com.ciandt.summit.bootcamp2022.integration.test;

import com.ciandt.summit.bootcamp2022.controller.MusicController;
import com.ciandt.summit.bootcamp2022.dto.AuthRequest;
import com.ciandt.summit.bootcamp2022.service.MusicService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = MusicController.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration
@ExtendWith(MockitoExtension.class)
public class MusicServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private MusicService musicService;

    private String token;


    @BeforeEach
    public void setUp(WebApplicationContext context) {
        //mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }


    @Test
    public void getAToken() throws Exception{

        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("jose");

      /*  RequestBuilder requestBuilder = MockMvcRequestBuilders.post("http://localhost:8080/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(authRequest));
         //new ObjectMapper().writeValueAsString(authRequest)
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        this.token = mvcResult.getResponse().getContentAsString();*/

        // Boolean assertMusic = this.token.getClass().equals(String.class);
        //Assertions.assertTrue(assertMusic);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(authRequest)))
                .andExpect(status().isOk());
    }

    @Test
    public void searchForAMusic() throws Exception{

  /*      RequestBuilder requestBuilder = MockMvcRequestBuilders.post("http://api/v1/musicas")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("The Beatles");*/

        /*MvcResult mvcResult = mockMvc*/
        mockMvc.perform(post("/api/v1/musicas?filtro=I cry instead"));
        //  String responseBodyAsString = mvcResult.getResponse().getContentAsString();
        // Boolean assertMusic = responseBodyAsString.contains("I cry instead");
        // Assertions.assertTrue(assertMusic);
    }






}