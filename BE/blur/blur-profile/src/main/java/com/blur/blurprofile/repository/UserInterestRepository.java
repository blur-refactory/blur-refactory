package com.blur.blurprofile.repository;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blur.blurprofile.entity.UserInterest;
import com.blur.blurprofile.entity.UserProfile;

public interface UserInterestRepository extends JpaRepository<UserInterest, Long> {

    public List<UserInterest> findByUserProfile(UserProfile userProfile);

    List<UserInterest> findAllByInterestInterestNameOrderByUserProfileUserIdDescSeqDesc(String interestName);

    @Query("SELECT ui.interest.interestName, COUNT(ui.seq) AS cnt " +
            "FROM UserInterest ui " +
            "WHERE ui.userProfile.userId IN (" +
            "   SELECT up.userId FROM UserProfile up " +
            "   JOIN up.userInterests ui2 " +
            "   WHERE ui2.interest.interestName = :interestName" +
            ") AND ui.interest.interestName <> :interestName " +
            "GROUP BY ui.interest.interestName " +
            "HAVING COUNT(ui.seq) > 1 " +
            "ORDER BY COUNT(ui.seq) DESC")
    List<Object[]> findPopularInterestsExcludingSelectedOne(@Param("interestName") String interestName, Pageable pageable);

    default List<Object[]> findPopularInterestsExcludingSelectedOne(String interestName) {
        return findPopularInterestsExcludingSelectedOne(interestName, PageRequest.of(0, 4));
    }
}
