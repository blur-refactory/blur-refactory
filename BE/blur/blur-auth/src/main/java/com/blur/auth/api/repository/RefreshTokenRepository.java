package com.blur.auth.api.repository;

import com.blur.auth.api.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    @Query(value = "select rt from RefreshToken rt where rt.member.email =:memail")
    Optional<RefreshToken> findByMemberEmail(@Param("memail") String email);

    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
