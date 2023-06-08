package com.blur.bluruser.match.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@NoArgsConstructor
@Getter
@AllArgsConstructor
@Builder
@Setter
@ApiModel(description = "ResponseMatchSettingDto")
public class ResponseMatchSettingDto {

    @ApiModelProperty(value = "최대 거리", required = true)
    private Integer maxDistance;

    @ApiModelProperty(value = "최소 나이", required = true)
    private Integer minAge ;

    @ApiModelProperty(value = "최대 나이", required = true)
    private Integer maxAge;

}

