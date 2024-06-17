package com.beneboba.package_tracking.security.filter;

import com.beneboba.package_tracking.util.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtSecurityFilter extends OncePerRequestFilter {

    @Autowired
    JwtService jwtService;

    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
             HttpServletRequest request,
             HttpServletResponse response,
             FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        String jwt = null;
        final String username;

//        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
//            log.error("Authorization header is missing -> {}", authHeader);
//            filterChain.doFilter(request, response);
//            return;
//        }

        if (authHeader != null)
            jwt = authHeader.substring(7);

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    jwt = cookie.getValue();
                }
            }
        }

        if (jwt == null) {
            log.error("jwtToken is null");
            filterChain.doFilter(request, response);
            return;
        }

        username = jwtService.extractUsername(jwt);

        log.info("jwtToken -> " + jwt);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            log.info("checking authentication for user " + username);

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            log.info("checking authentication for userDetails " + userDetails);

            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }

}