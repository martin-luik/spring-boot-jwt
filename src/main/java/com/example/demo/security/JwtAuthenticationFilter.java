package com.example.demo.security;

import com.example.demo.entity.UserAccount;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static com.example.demo.security.JwtConstant.AUTHENTICATION_HEADER;
import static com.example.demo.security.JwtConstant.TOKEN_TYPE;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            UserAccount userAccount = new ObjectMapper().readValue(request.getInputStream(), UserAccount.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userAccount.getUsername(), userAccount.getPassword()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authentication) throws IOException {
        ZonedDateTime expirationTokeTime = ZonedDateTime.now(ZoneOffset.UTC).plus(864_000_000L, ChronoUnit.MILLIS);
        String token = Jwts.builder()
                .setSubject(((User) authentication.getPrincipal()).getUsername())
                .setExpiration(Date.from(expirationTokeTime.toInstant()))
                .signWith(SignatureAlgorithm.HS256, "Secret")
                .compact();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader(AUTHENTICATION_HEADER, TOKEN_TYPE + token);
        response.getWriter().write("{ \"token\": \"" + token + "\" }\n");
    }

}
