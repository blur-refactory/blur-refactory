package com.blur.auth.api.dto;

import lombok.*;

@Builder
@RequiredArgsConstructor
@Getter
public class CheckIdRes {
    String code;
    String msg;

    public CheckIdRes(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
