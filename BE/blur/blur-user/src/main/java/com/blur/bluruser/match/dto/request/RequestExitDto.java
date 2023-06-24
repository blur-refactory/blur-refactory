package com.blur.bluruser.match.dto.request;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@AllArgsConstructor
@Setter
@ApiModel(description = "MeetingDto")
public class RequestExitDto {

    @ApiModelProperty(notes = "파트너 ID")
    private String partnerId;

    @ApiModelProperty(notes = "미팅 시간")
    private Integer playTime;
}
