package com.beneboba.package_tracking.service.impl;

import com.beneboba.package_tracking.entity.User;
import com.beneboba.package_tracking.entity.UserRole;
import com.beneboba.package_tracking.model.request.LoginRequest;
import com.beneboba.package_tracking.model.request.UserRegisterRequest;
import com.beneboba.package_tracking.model.response.LoginResponse;
import com.beneboba.package_tracking.model.response.UserRegisterResponse;
import com.beneboba.package_tracking.repository.UserRepository;
import com.beneboba.package_tracking.service.AuthService;
import com.beneboba.package_tracking.util.JwtService;
import com.beneboba.package_tracking.util.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final ValidationService validationService;

    private final   JwtService jwtService;

    private final  AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public UserRegisterResponse register(UserRegisterRequest registerRequest) {
        validationService.validate(registerRequest);

        Optional<User> user = userRepository.findUserByUsername(registerRequest.getUsername());

        if (user.isPresent()) {

            log.error("Register -> Username already exists");

            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }

        registerRequest.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        if (registerRequest.getRole() == null) {
            registerRequest.setRole(UserRole.USER);
        }

        User result = userRepository.save(registerRequest.toUserEntity());

        log.info("Register -> " + result.toString());

        return UserRegisterResponse.builder()
                .id(result.getId())
                .name(result.getName())
                .username(result.getUsername())
                .build();
    }

    @Override
    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {
        validationService.validate(loginRequest);

        log.info("login " + loginRequest);
        Optional<User> user = userRepository.findUserByUsername(loginRequest.getUsername());

        if (user.isEmpty()) {
            log.error("login -> Username or password wrong");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password wrong");
        }

        if (passwordEncoder.matches(loginRequest.getPassword(), user.get().getPassword())) {

            //TODO: Need refactor only using authmanager, remove reudndant call to db
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            String jwtToken = jwtService.generateToken(user.get());


            return LoginResponse
                    .builder()
                    .token(jwtToken)
                    .expiresIn(jwtService.getExpirationTime())
                    .build();

        } else {
            log.error(this.getClass().getSimpleName() + "login -> Username or password wrong");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password wrong");
        }
    }
}
