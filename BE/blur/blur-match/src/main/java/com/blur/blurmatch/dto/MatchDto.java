package com.blur.blurmatch.dto;

import com.blur.blurmatch.dto.request.RequestMatchDto;
import com.blur.blurmatch.dto.response.ResponseProfileDto;
import com.blur.blurmatch.entity.MatchMakingRating;
import com.blur.blurmatch.entity.MatchSetting;
import lombok.*;

@NoArgsConstructor
@Getter
@AllArgsConstructor
@Setter
public class MatchDto {

    private String userId;

    private String gender;

    private Integer age;

    private Integer point;

    private double lat;

    private double lng;

    private Integer maxDistance;

    private Integer minAge ;

    private Integer maxAge;

    public MatchDto(RequestMatchDto requestMatchDto, MatchSetting matchSetting, MatchMakingRating matchMakingRating, ResponseProfileDto responseProfile) {
        this.userId = matchSetting.getUserId();
        this.gender = responseProfile.getGender();
        this.age = responseProfile.getAge();
        this.point = matchMakingRating.getPoint();
        this.lat = requestMatchDto.getLat();
        this.lng = requestMatchDto.getLng();
        this.maxDistance = matchSetting.getMaxDistance();
        this.minAge = matchSetting.getMinAge();
        this.maxAge = matchSetting.getMaxAge();
    }


}
