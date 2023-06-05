package com.blur.blurprofile.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blur.blurprofile.entity.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, String> {
    public UserProfile findByUserId(String userId);
    
}
