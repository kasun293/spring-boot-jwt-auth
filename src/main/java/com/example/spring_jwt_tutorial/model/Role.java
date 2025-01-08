package com.example.spring_jwt_tutorial.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;

    @ManyToMany
    @JoinTable(name = "role_permission",
        joinColumns = {
            @JoinColumn(name = "role_id", referencedColumnName = "id")
        },
            inverseJoinColumns = {
            @JoinColumn(name = "permission_id", referencedColumnName = "id")
            }
    )
    private List<Permission> permissions;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;
}
