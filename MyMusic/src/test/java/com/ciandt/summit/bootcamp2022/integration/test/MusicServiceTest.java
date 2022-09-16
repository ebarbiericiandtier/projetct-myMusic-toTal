package com.ciandt.summit.bootcamp2022.integration.test;

import com.ciandt.summit.bootcamp2022.controller.AuthenticationController;
import com.ciandt.summit.bootcamp2022.controller.MusicController;
import com.ciandt.summit.bootcamp2022.dto.AuthRequest;
import com.ciandt.summit.bootcamp2022.service.MusicService;
import com.ciandt.summit.bootcamp2022.service.TokenProviderService;
import com.ciandt.summit.bootcamp2022.service.impl.MusicServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.http.parser.Authorization;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {AuthenticationController.class, MusicController.class})
@AutoConfigureMockMvc
//@ContextConfiguration
//@ExtendWith(MockitoExtension.class)
//@SpringBootTest
//@RunWith(MockitoJUnitRunner.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MusicServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MusicService musicService;

    @MockBean
    private AuthenticationController authenticationController;

    @MockBean
    TokenProviderService tokenProviderService;

    private String token;


   /* @BeforeEach
    public void setUp(WebApplicationContext context) {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }*/


    @Test
    public void getAToken() throws Exception{

        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("jose");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username","jose");

       RequestBuilder requestBuilder = MockMvcRequestBuilders.post("http://localhost:8080/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObject.toString());
         //new ObjectMapper().writeValueAsString(authRequest)
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();


        MockHttpServletResponse response = mvcResult.getResponse();
        System.out.println("HERE---------------->"+token);

       // Boolean assertMusic = this.token.getClass().equals(String.class);
       // Assertions.assertTrue(assertMusic);

        /*mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(authRequest)))
                .andExpect(status().isOk());*/
    }

    public void searchForAMusic() throws Exception{

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("http://api/v1/musicas")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,this.token)
                .content("The Beatles");

        MvcResult mvcResult =   mockMvc.perform(requestBuilder).andReturn();
        String responseBodyAsString = mvcResult.getResponse().getContentAsString();
        Boolean assertMusic = responseBodyAsString.contains("I cry instead");
         Assertions.assertTrue(assertMusic);
    }






}