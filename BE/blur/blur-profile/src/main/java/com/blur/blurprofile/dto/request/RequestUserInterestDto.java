package com.blur.blurprofile.dto.request;

import com.blur.blurprofile.entity.Interest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@ApiModel(value = "RequestUserInterestDto")
@NoArgsConstructor
@Getter
@Setter
public class RequestUserInterestDto {

    @ApiModelProperty(value = "관심사 목록", required = true)
    private List<String> Interests;

    public RequestUserInterestDto(List<String> interests) {
        this.Interests = interests;
    }

}
