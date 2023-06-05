package com.blur.blurmatch.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@NoArgsConstructor
@Getter
@AllArgsConstructor
@Builder
@Setter
@ApiModel(description = "MatchSettingDto")
public class MatchSettingDto {

    @ApiModelProperty(value = "사용자 ID", example = "blur")
    private String userId;

    @ApiModelProperty(value = "최대 거리", example = "100")
    private Integer maxDistance;

    @ApiModelProperty(value = "최소 나이", example = "20")
    private Integer minAge ;

    @ApiModelProperty(value = "최대 나이", example = "30")
    private Integer maxAge;

}
