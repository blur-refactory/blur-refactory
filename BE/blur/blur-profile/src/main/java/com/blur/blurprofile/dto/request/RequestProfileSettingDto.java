package com.blur.blurprofile.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ApiModel(value = "RequestProfileSettingDto")
public class RequestProfileSettingDto {

    @ApiModelProperty(value = "유저 아이디", required = true)
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
    private Integer minAge;

    @ApiModelProperty(value = "최대 나이")
    private Integer maxAge;
}
