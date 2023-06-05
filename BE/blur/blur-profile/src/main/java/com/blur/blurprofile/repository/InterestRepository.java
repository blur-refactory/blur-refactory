package com.blur.blurprofile.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blur.blurprofile.entity.Interest;

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
