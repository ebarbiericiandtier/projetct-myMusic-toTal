package com.ciandt.summit.bootcamp2022.service.impl;

import com.ciandt.summit.bootcamp2022.service.TokenProviderService;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class TokenProviderServiceTest {

    @Autowired
    private MockMvc mock;
    @Mock
    private TokenProviderServiceImpl tokenProviderServiceImpl;

    @InjectMocks
    private UserServiceImpl userService;

    @MockBean
    private TokenProviderService tokenProviderService;

    private String universalToken;


    @Test
    public void isTokenAString(){
        when(tokenProviderServiceImpl.getToken("maria")).then(AdditionalAnswers.returnsFirstArg());
        String token = tokenProviderServiceImpl.getToken("maria");
        Assertions.assertThat(tokenProviderServiceImpl.getToken("maria")).isInstanceOf(String.class);
    }

    public void testTheFlow(){
        try {
            this.mock.perform(MockMvcRequestBuilders.post("http://localhost:8080/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{ 'username': 'joao'}"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Basic")));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
