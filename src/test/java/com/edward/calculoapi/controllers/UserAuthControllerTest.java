package com.edward.calculoapi.controllers;

import com.edward.calculoapi.api.dto.requests.CreateAccountRequest;
import com.edward.calculoapi.api.dto.requests.LogInRequest;
import com.edward.calculoapi.api.dto.requests.TokenRefreshRequest;
import com.edward.calculoapi.security.services.UserDetailsImpl;
import com.edward.calculoapi.utils.MockUserFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Map;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@TestPropertySource(locations = "/application.properties")
public class UserAuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MockUserFactory mockUserFactory;

    @Test
    public void testItCanLogInAUserWhenTheyCreateAnAccount() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();

        CreateAccountRequest request = new CreateAccountRequest(
                "test",
                "test",
                Set.of("user"),
                "password",
                "email@email.com"
        );

        String requestJson = writer.writeValueAsString(request);

        this.mockMvc.perform(post("/api/v1/auth/register")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.jsonPath("$", hasValue(request.getFirstName())),
                        cookie().exists("calculo_token")
                );
    }
}
