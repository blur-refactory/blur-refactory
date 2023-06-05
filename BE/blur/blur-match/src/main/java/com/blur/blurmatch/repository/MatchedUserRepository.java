package com.blur.blurmatch.repository;

import com.blur.blurmatch.entity.MatchedUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchedUserRepository extends JpaRepository<MatchedUser, String> {

    MatchedUser findByUserId(String userId);
}
