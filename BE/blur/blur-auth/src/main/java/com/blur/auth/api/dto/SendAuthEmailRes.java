package com.blur.auth.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class SendAuthEmailRes {

    private String code;
    private String msg;
}
