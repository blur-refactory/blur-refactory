package com.blur.bluruser.profile.dto.response;

import com.blur.bluruser.match.entity.MatchSetting;
import com.blur.bluruser.profile.entity.Interest;
import com.blur.bluruser.profile.entity.UserProfile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ApiModel(description = "ResponseProfileSettingDto")
public class ResponseProfileSettingDto {

    @ApiModelProperty(value = "닉네임")
    private String nickname;

    @ApiModelProperty(value = "나이")
    private Integer age;

    @ApiModelProperty(value = "이미지")
    private String image;

    @ApiModelProperty(value = "성별")
    private String gender;

    @ApiModelProperty(value = "자기소개")
    private String introduce;

    @ApiModelProperty(value = "MBTI")
    private String mbti;

    @ApiModelProperty(value = "최대 거리")
    private Integer maxDistance;

    @ApiModelProperty(value = "최소 나이")
    private Integer minAge;

    @ApiModelProperty(value = "최대 나이")
    private Integer maxAge;

    @ApiModelProperty(value = "이메일")
    private String email;

    public ResponseProfileSettingDto(UserProfile userProfile, MatchSetting matchSetting) {
        this.nickname = userProfile.getNickname();
        this.age = userProfile.getAge();
        this.image = userProfile.getImage();
        this.gender = userProfile.getGender();
        this.introduce = userProfile.getIntroduce();
        this.mbti = userProfile.getMbti();
        this.maxDistance = matchSetting.getMaxDistance();
        this.minAge = matchSetting.getMinAge();
        this.maxAge = matchSetting.getMaxAge();
        this.email = userProfile.getUserId();
    }
}
