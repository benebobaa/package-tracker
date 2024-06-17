package com.beneboba.package_tracking.website.auth;

import com.beneboba.package_tracking.model.request.LoginRequest;
import com.beneboba.package_tracking.model.response.LoginResponse;
import com.beneboba.package_tracking.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
@Slf4j
public class WebAuthController{

    @Autowired
    private AuthService authService;

    @GetMapping("/login")
    public String showLogin(Model model) {
        LoginRequest loginRequest = LoginRequest.builder().build();
        model.addAttribute("loginRequest", loginRequest);
        return "auth/login";
    }

    @GetMapping("/register")
    public String showRegister() {
        return "auth/register";
    }


    @PostMapping("/login")
    public String login(
            @ModelAttribute LoginRequest loginRequest,
            HttpServletResponse response
    ) {
        log.info("login -> loginRequest :: {}", loginRequest);

        LoginResponse loginResponse = authService.login(loginRequest);

        ResponseCookie cookie = ResponseCookie.from("token", loginResponse.getToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(60 * 60 * 24 * 30 * 6)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        log.info("login -> cookie :: {}", cookie);
        log.info("login -> loginResponse :: {}", loginResponse);

        return "redirect:/home";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("token", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return "redirect:/auth/login";
    }
}
