package com.blur.bluruser.profile.repository;

import com.blur.bluruser.profile.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, String> {
    public UserProfile findByUserId(String userId);
    
}
