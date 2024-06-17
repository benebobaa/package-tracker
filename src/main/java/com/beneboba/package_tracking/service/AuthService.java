package com.beneboba.package_tracking.service;

import com.beneboba.package_tracking.model.request.LoginRequest;
import com.beneboba.package_tracking.model.request.UserRegisterRequest;
import com.beneboba.package_tracking.model.response.LoginResponse;
import com.beneboba.package_tracking.model.response.UserRegisterResponse;

public interface AuthService {
    UserRegisterResponse register(UserRegisterRequest registerRequest);
    LoginResponse login(LoginRequest loginRequest);
}
