package com.blur.bluruser.chat.service;

import com.blur.bluruser.chat.dto.ChatDto;
import com.blur.bluruser.chat.dto.LatestChatsResultDto;

public interface ChatMakeService {
    ChatDto saveChat(String userId, String roomId, String message);
    LatestChatsResultDto chatDtoList(String userId, double lastSocre);
    boolean deleteChat(String memberId, double deleteStart, double deleteEnd);
}
