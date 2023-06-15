package com.blur.bluruser.match.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel(description = "ResponseStartDto")
@NoArgsConstructor
@Getter
@Setter
public class ResponseStartDto {

    @ApiModelProperty(notes = "성별")
    private String gender;
}
