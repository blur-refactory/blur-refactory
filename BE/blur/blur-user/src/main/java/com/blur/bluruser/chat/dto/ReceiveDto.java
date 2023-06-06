package com.blur.bluruser.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReceiveDto {
    private String nickname;
    private String message;
    private String cursor;
    private String deleteStart;
    private String deleteEnd;
}
