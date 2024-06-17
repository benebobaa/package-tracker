package com.beneboba.package_tracking.service;

import com.beneboba.package_tracking.entity.User;
import com.beneboba.package_tracking.helper.dummy.DummyUser;
import com.beneboba.package_tracking.model.request.LoginRequest;
import com.beneboba.package_tracking.model.request.UserRegisterRequest;
import com.beneboba.package_tracking.model.response.LoginResponse;
import com.beneboba.package_tracking.model.response.UserRegisterResponse;
import com.beneboba.package_tracking.repository.UserRepository;
import com.beneboba.package_tracking.service.impl.AuthServiceImpl;
import com.beneboba.package_tracking.util.JwtService;
import com.beneboba.package_tracking.util.ValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ValidationService validationService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;


    @BeforeEach
    void setMockOutput() {
        doNothing().when(validationService).validate(any());
        lenient().when(passwordEncoder.encode(any())).thenReturn(DummyUser.USER_ENCODED_PASSWORD);
        lenient().when(jwtService.generateToken(any(User.class))).thenReturn(DummyUser.USER_JWT_TOKEN);
        lenient().when(jwtService.getExpirationTime()).thenReturn(DummyUser.USER_EXPIRATION);
    }

    @Test
    void testRegisterSuccess() {
        UserRegisterRequest registerRequest = DummyUser.newRegisterRequest();

        User userEntity = DummyUser.newUser();

        when(userRepository.findUserByUsername(DummyUser.USER_USERNAME)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(userEntity);

        UserRegisterResponse response = authService.register(registerRequest);

        verify(userRepository, times(1)).findUserByUsername(DummyUser.USER_USERNAME);
        verify(userRepository, times(1)).save(any(User.class));

        assertEquals(userEntity.getId(), response.getId());
        assertEquals(userEntity.getName(), response.getName());
        assertEquals(userEntity.getUsername(), response.getUsername());
    }

    @Test
    void testRegisterUsernameAlreadyExists() {
        UserRegisterRequest registerRequest = DummyUser.newRegisterRequest();

        User existingUser = DummyUser.newUser();

        when(userRepository.findUserByUsername(DummyUser.USER_USERNAME))
                .thenReturn(Optional.of(existingUser));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            authService.register(registerRequest);
        });

        assertEquals("Username already exists", exception.getReason());

        verify(userRepository, times(1))
                .findUserByUsername(DummyUser.USER_USERNAME);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testLoginSuccess() {
        LoginRequest loginRequest = DummyUser.newLoginRequest();

        User userEntity = DummyUser.newUser();

        when(userRepository.findUserByUsername(DummyUser.USER_USERNAME))
                .thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches(DummyUser.USER_PASSWORD, userEntity.getPassword()))
                .thenReturn(true);

        when(jwtService.generateToken(userEntity)).thenReturn(DummyUser.USER_JWT_TOKEN);
        when(jwtService.getExpirationTime()).thenReturn(DummyUser.USER_EXPIRATION);

        LoginResponse response = authService.login(loginRequest);

        verify(userRepository, times(1)).findUserByUsername(DummyUser.USER_USERNAME);
        verify(passwordEncoder, times(1))
                .matches(DummyUser.USER_PASSWORD, userEntity.getPassword());
        verify(jwtService, times(1))
                .generateToken(userEntity);

        assertNotNull(response);
        assertEquals(DummyUser.USER_JWT_TOKEN, response.getToken());
        assertEquals(DummyUser.USER_EXPIRATION, response.getExpiresIn());
    }

    @Test
    void testLoginUserNotFound() {
        LoginRequest loginRequest = DummyUser.newLoginRequest();
        when(userRepository.findUserByUsername(DummyUser.USER_USERNAME)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            authService.login(loginRequest);
        });

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("Username or password wrong", exception.getReason());

        verify(userRepository, times(1)).findUserByUsername(DummyUser.USER_USERNAME);
        verify(passwordEncoder, never()).matches(any(), any());
        verify(jwtService, never()).generateToken(any(User.class));
    }

    @Test
    void testLoginIncorrectPassword() {
        LoginRequest loginRequest = DummyUser.newLoginRequest();

        User userEntity = DummyUser.newUser();

        when(userRepository.findUserByUsername(DummyUser.USER_USERNAME)).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches(DummyUser.USER_PASSWORD, userEntity.getPassword())).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            authService.login(loginRequest);
        });

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());

        verify(userRepository, times(1)).findUserByUsername(DummyUser.USER_USERNAME);
        verify(passwordEncoder, times(1)).matches(DummyUser.USER_PASSWORD, userEntity.getPassword());
        verify(jwtService, never()).generateToken(any(User.class));
    }
}
