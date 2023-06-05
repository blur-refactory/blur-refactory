package com.blur.bluruser.chat.repository;

import com.blur.bluruser.chat.dto.ChatDto;
import com.blur.bluruser.chat.dto.LatestChatsResultDto;

public interface RedisChatRepository {
    void save(ChatDto chatDto);
    LatestChatsResultDto getLatestChats(String memberEmail, double lastScore);
}
