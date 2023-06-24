package com.blur.bluruser.profile.dto.request;

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
    private List<String> interests;
}
