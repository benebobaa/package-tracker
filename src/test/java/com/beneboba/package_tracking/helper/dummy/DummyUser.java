package com.beneboba.package_tracking.helper.dummy;

import com.beneboba.package_tracking.entity.User;
import com.beneboba.package_tracking.entity.UserRole;
import com.beneboba.package_tracking.model.request.LoginRequest;
import com.beneboba.package_tracking.model.request.UserRegisterRequest;

public class DummyUser {

    public static final String USER_NAME = "BENEDIKTUS SATRIYA";
    public static final String USER_USERNAME = "BENE";
    public static final String USER_PASSWORD = "beneboba123";
    public static final String USER_ENCODED_PASSWORD = "encodedpassword";
    public static final UserRole USER_ROLE = UserRole.USER;
    public static final String USER_JWT_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJJc3N1ZXIiOiJJc3N1ZXIiLCJVc2VybmFtZSI6IkphdmFJblVzZSIsImV4cCI6NDExNjc0NDAxOSwiaWF0IjoxNzE4NDUyODQ1fQ.TzMgUvwEEVGl2L0_hYfgloVK46Oeef-FodEWIrCgPog";
    public static final Long USER_EXPIRATION = 1416744019L;

    public static User newUser(){
        return new User(
                1L,
                USER_NAME,
                USER_USERNAME,
                USER_PASSWORD,
                USER_ROLE
        );
    }

    public static User newUser(UserRole role){
        return new User(
                1L,
                USER_NAME,
                USER_USERNAME,
                USER_PASSWORD,
                role
        );
    }

    public static LoginRequest newLoginRequest(){
        return LoginRequest
                .builder()
                .username(USER_USERNAME)
                .password(USER_PASSWORD)
                .build();
    }

    public static UserRegisterRequest newRegisterRequest(){
        return UserRegisterRequest
                .builder()
                .name(USER_NAME)
                .username(USER_USERNAME)
                .password(USER_PASSWORD)
                .confirmPassword(USER_PASSWORD)
                .build();
    }
}
