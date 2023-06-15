package com.blur.auth.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChangePasswordReq {
    private String email;
    private String password;
}
