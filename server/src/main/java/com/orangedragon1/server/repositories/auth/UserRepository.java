package com.orangedragon1.server.repositories.auth;

import com.orangedragon1.server.models.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    // integer because @Id is integer

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
