package com.blur.bluruser.match.repository;

import com.blur.bluruser.match.entity.MatchSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchSettingRepository extends JpaRepository<MatchSetting, String> {

    MatchSetting findByUserId(String userId);
}
