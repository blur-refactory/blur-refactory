package com.blur.bluruser.match.dto.response;

import com.blur.bluruser.match.dto.MatchDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ResponseCheckDto {

    @ApiModelProperty(notes = "매칭된 파트너의 아이디")
    private String partnerId;

    @ApiModelProperty(notes = "세션 아이디")
    private String sessionId;

    public ResponseCheckDto(MatchDto partner) {

        this.partnerId = partner.getUserId();
    }
}

