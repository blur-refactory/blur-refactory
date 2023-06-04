package com.blur.bluruser.repository;

import com.blur.bluruser.entity.MatchMakingRating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchMakingRatingRepository extends JpaRepository<MatchMakingRating, String> {

    MatchMakingRating findByUserId(String userId);
}
