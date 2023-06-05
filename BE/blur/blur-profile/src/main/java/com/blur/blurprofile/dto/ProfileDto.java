package com.blur.blurprofile.dto;

import com.blur.blurprofile.entity.Interest;
import com.blur.blurprofile.entity.UserInterest;
import com.blur.blurprofile.entity.UserProfile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Setter
@ApiModel(value = "ProfileDto", description = "ProfileDTO")
public class ProfileDto {

    @ApiModelProperty(value = "사용자 ID", required = true)
    private String userId;

    @ApiModelProperty(value = "사용자 나이")
    private Integer age;

    @ApiModelProperty(value = "사용자 닉네임")
    private String nickname;

    @ApiModelProperty(value = "사용자 이미지 경로")
    private String image;

    @ApiModelProperty(value = "사용자 성별")
    private String gender;

    @ApiModelProperty(value = "사용자 소개")
    private String introduce;

    @ApiModelProperty(value = "사용자 MBTI 유형")
    private String mbti;

}
