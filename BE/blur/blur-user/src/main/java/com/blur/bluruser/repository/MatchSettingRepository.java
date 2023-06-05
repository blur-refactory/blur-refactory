package com.blur.bluruser.repository;

import com.blur.bluruser.entity.MatchSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchSettingRepository extends JpaRepository<MatchSetting, String> {

    MatchSetting findByUserId(String userId);
}
