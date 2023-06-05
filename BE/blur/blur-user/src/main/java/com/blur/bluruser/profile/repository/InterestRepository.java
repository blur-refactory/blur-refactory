package com.blur.bluruser.profile.repository;

import com.blur.bluruser.profile.entity.Interest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterestRepository extends JpaRepository<Interest, String> {

    public List<Interest> findAll();

    public Interest findByInterestName(String interestName);
    
//    @Query("SELECT i, COUNT(ui) " +
//            "FROM Interest i LEFT JOIN i.userInterests ui " +
//            "WHERE i.interestName = :interestName " +
//            "GROUP BY i " +
//            "ORDER BY COUNT(ui) DESC")
//     List<Object[]> countTopUserInterestsByInterest(@Param("interestName") String interestName);
}
