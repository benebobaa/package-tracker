package com.beneboba.package_tracking.controller;

import com.beneboba.package_tracking.model.BaseResponse;
import com.beneboba.package_tracking.model.request.LoginRequest;
import com.beneboba.package_tracking.model.request.UserRegisterRequest;
import com.beneboba.package_tracking.model.response.LoginResponse;
import com.beneboba.package_tracking.model.response.UserRegisterResponse;
import com.beneboba.package_tracking.service.AuthService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private AuthController authController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Test
    void testRegisterSuccess() throws Exception {
        UserRegisterRequest request = UserRegisterRequest
                .builder()
                .username("beneboba")
                .name("benediktus")
                .password("beneboba123")
                .confirmPassword("beneboba123")
                .build();

        UserRegisterResponse response =  UserRegisterResponse
                .builder()
                .id(1L)
                .username("beneboba")
                .name("benediktus")
                .build();

        when(authService.register(any(UserRegisterRequest.class))).thenReturn(response);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/auth/register")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value(request.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value(request.getName()));

        verify(authService, times(1)).register(any(UserRegisterRequest.class));
    }

    @Test
    void testLoginSuccess() throws Exception {
        LoginRequest request = LoginRequest
                .builder()
                .username("beneboba")
                .password("beneboba123")
                .build();
        LoginResponse response = LoginResponse
                .builder()
                .token("tokenzzz")
                .expiresIn(1234567890)
                .build();

        when(authService.login(any(LoginRequest.class))).thenReturn(response);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/auth/login")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.token").value(response.getToken()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.expiresIn").value(response.getExpiresIn()));

        verify(authService, times(1)).login(any(LoginRequest.class));
    }

}
