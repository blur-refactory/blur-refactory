package com.blur.bluruser.chat.service;

import com.blur.bluruser.chat.dto.ChatDto;
import com.blur.bluruser.chat.dto.ReceiveDto;

public interface ChatSendService {
    ChatDto sendChat(String userId, String roodId, ReceiveDto receivedMessage) throws Exception;
}
