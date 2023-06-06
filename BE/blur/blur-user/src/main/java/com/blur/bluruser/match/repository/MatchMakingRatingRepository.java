package com.blur.bluruser.match.repository;

import com.blur.bluruser.match.entity.MatchMakingRating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchMakingRatingRepository extends JpaRepository<MatchMakingRating, String> {

    MatchMakingRating findByUserId(String userId);
}
