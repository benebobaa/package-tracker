package com.beneboba.package_tracking.controller;

import com.beneboba.package_tracking.model.BaseResponse;
import com.beneboba.package_tracking.model.request.LoginRequest;
import com.beneboba.package_tracking.model.request.UserRegisterRequest;
import com.beneboba.package_tracking.model.response.LoginResponse;
import com.beneboba.package_tracking.model.response.UserRegisterResponse;
import com.beneboba.package_tracking.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<UserRegisterResponse>> register(@RequestBody UserRegisterRequest request) {

        log.info("register -> " + request.toString());

        UserRegisterResponse response = authService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BaseResponse
                        .<UserRegisterResponse>builder()
                        .data(response)
                        .build());
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<LoginResponse>> login(@RequestBody LoginRequest request) {

        log.info("authenticate -> " + request.toString());

        LoginResponse response = authService.login(request);

        return ResponseEntity.ok(BaseResponse
                .<LoginResponse>builder()
                .data(response)
                .build());
    }
}