package com.blur.bluruser.match.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@ApiModel(description = "RequestFemaleCheckDto")
@NoArgsConstructor
@Getter
@AllArgsConstructor
public class RequestFemaleCheckDto {

    @ApiModelProperty(notes = "세션 아이디")
    private String sessionId;
}
