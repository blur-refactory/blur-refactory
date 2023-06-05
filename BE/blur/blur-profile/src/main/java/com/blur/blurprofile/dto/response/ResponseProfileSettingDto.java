package com.blur.blurprofile.dto.response;

import com.blur.blurprofile.dto.MatchSettingDto;
import com.blur.blurprofile.dto.ProfileDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ApiModel(description = "ResponseProfileSettingDto")
public class ResponseProfileSettingDto {

    @ApiModelProperty(value = "사용자 ID")
    private String userId;

    @ApiModelProperty(value = "나이")
    private Integer age;

    @ApiModelProperty(value = "닉네임")
    private String nickname;

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
    private Integer minAge ;

    @ApiModelProperty(value = "최대 나이")
    private Integer maxAge;

    @ApiModelProperty(value = "이메일")
    private String email;

    public ResponseProfileSettingDto(ProfileDto profileDto, MatchSettingDto matchSettingDto, String Email) {
        this.userId = profileDto.getUserId();
        this.age = profileDto.getAge();
        this.nickname = profileDto.getNickname();
        this.image = profileDto.getImage();
        this.gender = profileDto.getGender();
        this.introduce = profileDto.getIntroduce();
        this.mbti = profileDto.getMbti();
        this.maxDistance = matchSettingDto.getMaxDistance();
        this.minAge = matchSettingDto.getMinAge();
        this.maxAge = matchSettingDto.getMaxAge();
        this.email = Email;
    }
}
