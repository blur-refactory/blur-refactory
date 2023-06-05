package com.blur.bluruser.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MakeChatroomDto {
    private String maleEmail;
    private String femaleEmail;
    private String maleName;
    private String femaleName;
}
