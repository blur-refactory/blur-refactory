package com.blur.bluruser.chat.repository;

import com.blur.bluruser.chat.dto.ChatDto;
import com.blur.bluruser.chat.dto.LatestChatsResultDto;

public interface RedisChatRepository {
    void save(ChatDto chatDto);
    LatestChatsResultDto getLatestChats(String roomId, double lastScore);
    boolean delete(String roomId, double deleteStart, double deleteEnd);
}
