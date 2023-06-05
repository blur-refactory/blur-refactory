package com.blur.blurprofile.dto.response;

import com.blur.blurprofile.entity.Interest;
import com.blur.blurprofile.entity.UserProfile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Setter
@ApiModel(value = "ResponseCardDto")
public class ResponseCardDto {

    @ApiModelProperty(value = "닉네임")
    private String nickname;

    @ApiModelProperty(value = "나이")
    private Integer age;

    @ApiModelProperty(value = "소개")
    private String introduce;

    @ApiModelProperty(value = "성별")
    private String gender;

    @ApiModelProperty(value = "이미지")
    private String image;

    @ApiModelProperty(value = "사용자 관심사")
    private List<Interest> userInterests;

    public ResponseCardDto(UserProfile userProfile, List<Interest> userInterests) {
        this.nickname = userProfile.getNickname();
        this.age = userProfile.getAge();
        this.introduce = userProfile.getIntroduce();
        this.gender = userProfile.getGender();
        this.image = userProfile.getImage();
        this.userInterests = userInterests;
    }
}
