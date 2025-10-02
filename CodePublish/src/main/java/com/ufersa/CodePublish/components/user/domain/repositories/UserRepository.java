package com.ufersa.CodePublish.components.user.domain.repositories;

import com.ufersa.CodePublish.components.user.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query(value = "SELECT u FROM User u WHERE u.email=:email")
    Optional<User> getByEmail(@Param("email") String email);

    UserDetails findByEmail(String email);
}
