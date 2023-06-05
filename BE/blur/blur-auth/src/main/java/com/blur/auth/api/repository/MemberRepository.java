package com.blur.auth.api.repository;

import com.blur.auth.api.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {
    Optional<Member> findById(String id);
    Optional<Member> findByEmail(String email);

    @Query("select m from Member m where m.refreshToken.refreshToken =:rt")
    Optional<Member> findByRefreshToken(@Param("rt") String refreshToken);
}
