package com.example.spring_jwt_tutorial.config;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // validate token
        try {
            String token = null;
            String username = null;
            Claims claims = null;
            boolean authenticated = false;
            if (request.getHeader("Authorization") != null && request.getHeader("Authorization").startsWith("Bearer ")) {
                String credentials = request.getHeader("Authorization");
                token = credentials.substring(7);
                if (jwtTokenProvider.validateToken(token)) {
                    username = jwtTokenProvider.getUserNameFromToken(token);
                    claims = jwtTokenProvider.getClaimsFromToken(token);
                    authenticated = true;
                    List<Map<String, String>> userDetailMapList = null;
                    userDetailMapList = (List<Map<String, String>>) claims.get("permissions");
                    List<String> authoritiesList = userDetailMapList.stream().map(map -> map.get("authority"))
                            .collect(Collectors.toList());

                    Set<SimpleGrantedAuthority> authoritiesSet = new HashSet<>();
                    for (String authority : authoritiesList) {
                        authoritiesSet.add(new SimpleGrantedAuthority(authority));
                    }
                    JwtAuthenticationToken jwtAuthenticationToken =
                            new JwtAuthenticationToken(token, authoritiesSet, username, authenticated, claims);
                    SecurityContextHolder.getContext().setAuthentication(jwtAuthenticationToken);
                }
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }
        filterChain.doFilter(request, response);
    }
}
