package com.blur.bluruser.repository;

import com.blur.bluruser.entity.MatchedUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchedUserRepository extends JpaRepository<MatchedUser, String> {

    MatchedUser findByUserId(String userId);
}
