package com.blur.auth.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SendAuthEmailReq {
    private String email;
}
