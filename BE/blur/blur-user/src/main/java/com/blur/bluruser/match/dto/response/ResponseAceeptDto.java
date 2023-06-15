package com.blur.bluruser.match.dto.response;

import com.blur.bluruser.match.dto.request.RequestAcceptDto;
import com.blur.bluruser.profile.entity.UserProfile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

@ApiModel(description = "ResponseAcceptDto")
@NoArgsConstructor
@Getter
public class ResponseAceeptDto {

    @ApiModelProperty(notes = "파트너 닉네임")
    private String partnerNickname;

    @ApiModelProperty(notes = "파트너 관심사")
    private Collection<String> partnerInterests;

    public ResponseAceeptDto(UserProfile partner, List<String> partnerInterests) {

        this.partnerNickname = partner.getNickname();
        this.partnerInterests = partnerInterests;
    }
}
