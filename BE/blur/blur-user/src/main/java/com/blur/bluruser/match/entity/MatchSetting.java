package com.blur.bluruser.match.entity;

import com.blur.bluruser.match.dto.request.RequestUpdateSettingDto;
import com.blur.bluruser.match.dto.response.ResponseMatchSettingDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@NoArgsConstructor
@Builder
@Table(name = "matching_setting")
public class MatchSetting {

    @JsonIgnore
    @Column(name = "user_id")
    @Id
    private String userId;

    @Column(name = "max_distance")
    private Integer maxDistance;

    @Column(name = "min_age")
    private Integer minAge;

    @Column(name = "max_age")
    private Integer maxAge;

    @Builder
    public MatchSetting(String userId, Integer maxDistance, Integer minAge, Integer maxAge) {
        this.userId = userId;
        this.maxDistance = maxDistance;
        this.minAge = minAge;
        this.maxAge = maxAge;
    }

    public void update(RequestUpdateSettingDto requestUpdateSettingDto) {
        this.maxDistance = requestUpdateSettingDto.getMaxDistance();
        this.maxAge = requestUpdateSettingDto.getMaxAge();
        this.minAge = requestUpdateSettingDto.getMinAge();
    }
}
