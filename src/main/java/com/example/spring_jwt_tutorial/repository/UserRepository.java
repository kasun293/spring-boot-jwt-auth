package com.example.spring_jwt_tutorial.repository;

import com.example.spring_jwt_tutorial.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    @Query("SELECT r.code FROM Role r JOIN r.users u WHERE u.username = :username")
    List<String> findRoleCodesByUsername(@Param("username") String username);

    @Query("SELECT p.code FROM Permission p JOIN p.roles r WHERE r.code IN (:roleCodes)")
    List<String> findPermissionsByRoleCodes(@Param("roleCodes") List<String> roleCodes);
}
