package com.blur.blurmatch.dto.response;

import com.blur.blurmatch.dto.MatchDto;
import com.blur.blurmatch.dto.request.RequestCheckDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ApiModel(description = "ResponseMatchDto")
public class ResponseMatchDto {

    @ApiModelProperty(notes = "자신의 성별")
    private String myGender;

    @ApiModelProperty(notes = "매칭된 파트너의 아이디")
    private String partnerId;

    @ApiModelProperty(notes = "세션 아이디")
    private String sessionId;

    public ResponseMatchDto(MatchDto femaleDto, MatchDto selectedMaleDto) {
        this.myGender = femaleDto.getGender();
        this.partnerId = selectedMaleDto.getUserId();
    }

}
