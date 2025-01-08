package com.example.spring_jwt_tutorial.config;

import io.jsonwebtoken.Claims;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.security.auth.Subject;
import java.util.Collection;

@Data
public class JwtAuthenticationToken implements Authentication {


    private String token;
    private Collection<? extends GrantedAuthority> authorities;
    private String username;
    private boolean authenticated;
    private Claims claims;

    public JwtAuthenticationToken(String token, Collection<SimpleGrantedAuthority> authorities, String username, boolean authenticated, Claims claims) {
        this.token = token;
        this.authorities = authorities;
        this.username = username;
        this.authenticated = authenticated;
        this.claims = claims;

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return username;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return username;
    }

    @Override
    public boolean implies(Subject subject) {
        return Authentication.super.implies(subject);
    }
}
