package com.blur.auth.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Builder
@Getter
public class ChangePasswordRes {
    private String msg;
    private String code;
}
