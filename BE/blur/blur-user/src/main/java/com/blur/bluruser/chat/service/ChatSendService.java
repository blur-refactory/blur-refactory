package com.blur.bluruser.chat.service;

import com.blur.bluruser.chat.dto.ChatDto;

public interface ChatSendService {
    ChatDto sendChat(String userId, String roodId, String nickname, String message) throws Exception;
}
