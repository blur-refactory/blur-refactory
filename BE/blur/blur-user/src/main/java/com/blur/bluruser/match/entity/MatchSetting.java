package com.blur.bluruser.match.entity;

import com.blur.bluruser.match.dto.request.RequestUpdateSettingDto;
import com.blur.bluruser.match.dto.response.ResponseMatchSettingDto;
import com.blur.bluruser.profile.entity.UserProfile;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Getter
@Builder
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "matching_setting")
public class MatchSetting {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "max_distance")
    @ColumnDefault("30")
    private Integer maxDistance;

    @Column(name = "min_age")
    @ColumnDefault("20")
    private Integer minAge;

    @Column(name = "max_age")
    @ColumnDefault("40")
    private Integer maxAge;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private UserProfile userProfile;

    public void update(RequestUpdateSettingDto requestUpdateSettingDto) {
        this.maxDistance = requestUpdateSettingDto.getMaxDistance();
        this.maxAge = requestUpdateSettingDto.getMaxAge();
        this.minAge = requestUpdateSettingDto.getMinAge();
    }
}
