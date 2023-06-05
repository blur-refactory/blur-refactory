package com.blur.blurmatch.dto;


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
public class MeetingDto {

    @ApiModelProperty(notes = "사용자 ID")
    private String userId;

    @ApiModelProperty(notes = "파트너 ID")
    private String partnerId;

    @ApiModelProperty(notes = "미팅 시간")
    private Integer playTime;
}
