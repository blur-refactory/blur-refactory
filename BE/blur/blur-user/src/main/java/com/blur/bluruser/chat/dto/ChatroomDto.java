package com.blur.bluruser.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatroomDto {
    private String id;
    private String MyName;
    private String OpponentName;
    private String MyId;
    private String OpponentId;
    private String OpponentImage;
    private String lastestMessage;
    private String lastestMessageTime;
}
