package com.blur.auth.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserSignUpReq {
    private String email;
    private String password;
}
