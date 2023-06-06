package com.blur.auth.api.repository;

import com.blur.auth.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findById(String userId);
    @Query("select m from User m where m.refreshToken.refreshToken =:rt")
    Optional<User> findByRefreshToken(@Param("rt") String refreshToken);
}
