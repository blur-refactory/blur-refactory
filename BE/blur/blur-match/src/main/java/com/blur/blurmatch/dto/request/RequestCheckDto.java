package com.blur.blurmatch.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@ApiModel(description = "RequestCheckDto")
@NoArgsConstructor
@Getter
@AllArgsConstructor
public class RequestCheckDto {

    @ApiModelProperty(notes = "사용자 ID")
    private String userId;

    @ApiModelProperty(notes = "위도")
    private double lat;

    @ApiModelProperty(notes = "경도")
    private double lng;

    @ApiModelProperty(notes = "사용자 성별")
    private String gender;
}
