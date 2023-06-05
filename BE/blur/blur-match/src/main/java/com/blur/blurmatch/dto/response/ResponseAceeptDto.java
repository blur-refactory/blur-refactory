package com.blur.blurmatch.dto.response;

import com.blur.blurmatch.dto.request.RequestAcceptDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collection;

@ApiModel(description = "ResponseAcceptDto")
@NoArgsConstructor
@Getter
public class ResponseAceeptDto {

    @ApiModelProperty(notes = "파트너 ID")
    private String partnerId;

    @ApiModelProperty(notes = "파트너 닉네임")
    private String partnerNickname;

    @ApiModelProperty(notes = "파트너 관심사")
    private Collection<String> partnerInterests;

    @ApiModelProperty(notes = "세션 ID")
    private String sessionId;

    public ResponseAceeptDto(String partnerId, String partnerNickname, Collection<String> partnerInterests, String sessionId) {
        this.partnerId = partnerId;
        this.partnerNickname = partnerNickname;
        this.partnerInterests = partnerInterests;
        this.sessionId = sessionId;
    }

    public ResponseAceeptDto(RequestAcceptDto requestAcceptDto, String partnerNickname, Collection<String> partnerInterests) {
        this.partnerId = requestAcceptDto.getPartnerId();
        this.partnerNickname = partnerNickname;
        this.partnerInterests = partnerInterests;
        this.sessionId = requestAcceptDto.getSessionId();
    }
}
