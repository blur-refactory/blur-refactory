package com.blur.bluruser.match.repository;

import com.blur.bluruser.match.entity.MatchedUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchedUserRepository extends JpaRepository<MatchedUser, String> {

    MatchedUser findByUserId(String userId);
}
