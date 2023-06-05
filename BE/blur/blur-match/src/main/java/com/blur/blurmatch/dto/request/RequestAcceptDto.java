package com.blur.blurmatch.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@ApiModel(description = "RequestAcceptDto")
@NoArgsConstructor
@Getter
@AllArgsConstructor
public class RequestAcceptDto {

    @ApiModelProperty(notes = "사용자 ID")
    private String userId;

    @ApiModelProperty(notes = "사용자 성별")
    private String myGender;

    @ApiModelProperty(notes = "파트너 ID")
    private String partnerId;

    @ApiModelProperty(notes = "세션 ID")
    private String sessionId;
}
