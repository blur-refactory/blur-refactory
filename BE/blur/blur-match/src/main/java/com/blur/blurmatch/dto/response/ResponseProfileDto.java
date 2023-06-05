package com.blur.blurmatch.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Setter
public class ResponseProfileDto {

    private String userId;

    private Integer age;

    private String nickname;

    private String image;

    private String gender;

    private String introduce;
}