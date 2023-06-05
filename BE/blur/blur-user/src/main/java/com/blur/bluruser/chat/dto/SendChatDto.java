package com.blur.bluruser.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendChatDto {
    private String message;
    private String formattedCreatedAt;
    private double score;
}
