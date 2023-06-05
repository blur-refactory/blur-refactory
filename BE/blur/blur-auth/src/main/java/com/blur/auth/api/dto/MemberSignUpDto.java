package com.blur.auth.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MemberSignUpDto {
    private String email;
    private String password;
}
