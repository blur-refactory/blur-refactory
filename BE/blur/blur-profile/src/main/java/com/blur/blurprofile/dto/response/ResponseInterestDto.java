package com.blur.blurprofile.dto.response;

import com.blur.blurprofile.entity.Interest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ApiModel(description = "InterestDto")
public class ResponseInterestDto {

    @ApiModelProperty(value = "전체 관심사 목록")
    private List<Interest> interests;

    @ApiModelProperty(value = "사용자 관심사 목록")
    private List<Interest> userInterests;

    public ResponseInterestDto(List<Interest> allInterests, List<Interest> userInterests) {
        this.interests = allInterests;
        this.userInterests = userInterests;
    }
}
