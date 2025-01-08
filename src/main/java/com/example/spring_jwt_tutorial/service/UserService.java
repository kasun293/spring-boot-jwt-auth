package com.example.spring_jwt_tutorial.service;

import com.example.spring_jwt_tutorial.config.JwtTokenProvider;
import com.example.spring_jwt_tutorial.model.AuthRequest;
import com.example.spring_jwt_tutorial.model.User;
import com.example.spring_jwt_tutorial.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService implements UserDetailsService {


    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public UserService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();

        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), authorities);
    }


    public void signUp(User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public String login(AuthRequest authRequest) {
        // validate auth request
        User user = userRepository.findByUsername(authRequest.getUsername());
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid Credentials");
        }
        List<String> roles = userRepository.findRoleCodesByUsername(authRequest.getUsername());
        List<String> permissions = userRepository.findPermissionsByRoleCodes(roles);
        Set<SimpleGrantedAuthority> grantedAuthorities = new HashSet<>();
        permissions.forEach(permission -> grantedAuthorities.add(new SimpleGrantedAuthority(permission)));
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(authRequest.getUsername(),
                user.getPassword(), grantedAuthorities);
        return jwtTokenProvider.generateToken(user.getUsername(), userDetails, roles);

    }
}
