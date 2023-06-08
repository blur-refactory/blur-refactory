package com.blur.bluruser.match.dto.request;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel(description = "RequestMatchDto")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class RequestMatchDto {

    @ApiModelProperty(notes = "사용자 위치의 위도")
    private double lat;

    @ApiModelProperty(notes = "사용자 위치의 경도")
    private double lng;

}
