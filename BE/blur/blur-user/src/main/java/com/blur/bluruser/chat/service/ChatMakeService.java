package com.blur.bluruser.chat.service;

import com.blur.bluruser.chat.dto.ChatDto;
import com.blur.bluruser.chat.dto.LatestChatsResultDto;
import com.blur.bluruser.chat.dto.ReceiveDto;

public interface ChatMakeService {
    ChatDto saveChat(String userId, String roomId, ReceiveDto receivedMessage);
    LatestChatsResultDto chatDtoList(String userId, double lastSocre);
    boolean deleteChat(String memberId, double deleteStart, double deleteEnd);
}
