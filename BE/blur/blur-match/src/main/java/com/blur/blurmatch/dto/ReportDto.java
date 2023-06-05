package com.blur.blurmatch.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@ApiModel(description = "ReportDto")
public class ReportDto {

    @ApiModelProperty(notes = "사용자 ID")
    String userId;

    @ApiModelProperty(notes = "신고할 유저 ID")
    String reportedUserId;

    @ApiModelProperty(notes = "신고 내용")
    String content;
}
