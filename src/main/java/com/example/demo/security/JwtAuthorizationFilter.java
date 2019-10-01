package com.example.demo.security;

import com.example.demo.service.CustomUserDetailService;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.demo.security.JwtConstant.AUTHORIZATION_HEADER;
import static com.example.demo.security.JwtConstant.TOKEN_TYPE;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final CustomUserDetailService customUserDetailService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, CustomUserDetailService customUserDetailService) {
        super(authenticationManager);
        this.customUserDetailService = customUserDetailService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(AUTHORIZATION_HEADER);
        if (header != null && header.startsWith(TOKEN_TYPE)) {
            UsernamePasswordAuthenticationToken usernamePasswordAuth = getAuthenticationToken(request);
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuth);
        }
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthenticationToken(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION_HEADER);
        if (token != null) {
            String username = Jwts.parser()
                    .setSigningKey("Secret")
                    .parseClaimsJws(token.replace(TOKEN_TYPE, ""))
                    .getBody()
                    .getSubject();
            if (username != null) {
                UserDetails userDetails = customUserDetailService.loadUserByUsername(username);
                return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            }
            return null;
        }
        return null;
    }
}
