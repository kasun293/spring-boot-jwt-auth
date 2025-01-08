package com.example.spring_jwt_tutorial.model;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest implements Serializable {

    @NotEmpty(message = "Username cannot be empty")
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @NotEmpty(message = "Password cannot be empty")
    private String password;
}
