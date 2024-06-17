package com.beneboba.package_tracking.controller;

import com.beneboba.package_tracking.helper.dummy.DummyUser;
import com.beneboba.package_tracking.model.BaseResponse;
import com.beneboba.package_tracking.model.request.LoginRequest;
import com.beneboba.package_tracking.model.request.UserRegisterRequest;
import com.beneboba.package_tracking.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ErrorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Test
    void testConstraintViolationException() throws Exception {

        LoginRequest badLoginRequest = DummyUser.newLoginRequest();

        doThrow(new ConstraintViolationException("Validation error", null))
                .when(authService).login(badLoginRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .content(objectMapper.writeValueAsString(badLoginRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testResponseStatusException() throws Exception {

        UserRegisterRequest badRegisterRequest = DummyUser.newRegisterRequest();

        doThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists"))
                .when(authService).register(badRegisterRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .content(objectMapper.writeValueAsString(badRegisterRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errors").value("Username already exists"));
    }

    @Test
    void testAuthenticationException() throws Exception {

        LoginRequest badLoginRequest = DummyUser.newLoginRequest();

        doThrow(new AuthenticationException("Bad credentials") {})
                .when(authService).login(badLoginRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .content(objectMapper.writeValueAsString(badLoginRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errors").value("Bad credentials"));
    }
}
